package com.sundeep.api.repository;

import com.sundeep.api.exception.InternalServerException;
import com.sundeep.api.exception.ResourceNotFoundException;
import com.sundeep.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String INSERT_USER_QUERY = "INSERT INTO USERS ( username, first_name, last_name, password, enabled ) VALUES ( :username, :firstName, :lastName, :password, :enabled )";
    private final static String INSERT_ROLES_QUERY = "INSERT INTO AUTHORITIES ( username, authority ) VALUES (:username, :authority)";
    private final static String USER_BASE_QUERY = "select u.username username, u.first_name firstName, u.last_name lastName, u.password password, u.enabled enabled, u.created_time createdTime, u.updated_time updatedTime";
    private final static String USERS_QUERY =  USER_BASE_QUERY + ", LISTAGG(a.authority, ',') WITHIN GROUP (ORDER BY a.authority) as roles from users u, authorities a where u.username=a.username group by username, firstName, lastName, password, enabled, createdTime, updatedTime ";
    private final static String USER_QUERY = USER_BASE_QUERY + " from users u where u.username=:username";
    private final static String USER_ROLE_QUERY = "select authority from authorities where username=:username";
    private final static String USER_UPDATE_QUERY = "update users set first_name=:firstName, last_name=:lastName, password=:password, updated_time=CURRENT_TIMESTAMP, enabled=:enabled where username=:username";
    private final static String DELETE_ROLES = "delete from authorities where username=:username";
    private final static String DELETE_USER = "delete from users where username=:username";

    @Transactional
    private boolean manageUser( final User user, final boolean isAdd) throws InternalServerException {
        try{
            final Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("firstName", user.getFirstName());
            params.put("lastName", user.getLastName());
            params.put("password", user.getPassword());
            params.put("enabled", user.isEnabled());
            final int result;
            if(isAdd){
                result = namedParameterJdbcTemplate.update( INSERT_USER_QUERY, params);
            }else{
                result = namedParameterJdbcTemplate.update( USER_UPDATE_QUERY, params);
            }

            if (result == 1  && user.getRoles() != null){
                if(!isAdd)
                {
                    namedParameterJdbcTemplate.update( DELETE_ROLES, params);
                }
                return addUserRoles(user.getUsername(),user.getRoles());
            }else{
                return false;
            }
        }catch(final Exception ex){
            throw new InternalServerException(ex.getMessage());
        }
    }

    @Transactional
    private boolean addUserRoles( final String username, final String roles )
    {
        if( roles.contains(",") )
        {
            List<String> roleList = Arrays.asList(StringUtils.commaDelimitedListToStringArray(roles));
            List<Map<String, Object>> batchValues = new ArrayList<>(roleList.size());
            for ( final String role : roleList) {
                batchValues.add(
                        new MapSqlParameterSource("username", username)
                                .addValue("authority", role)
                                .getValues());
            }
            namedParameterJdbcTemplate.batchUpdate(INSERT_ROLES_QUERY,
                    batchValues.toArray(new Map[roleList.size()]));
        }
        else
        {
            final Map<String, Object> params = new HashMap<>();
            params.put("username", username);
            params.put("authority", roles);
            namedParameterJdbcTemplate.update( INSERT_ROLES_QUERY, params);
        }
        return true;
    }

    @Transactional
    public boolean deleteUser(final String username){
        final Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        final int result = namedParameterJdbcTemplate.update( DELETE_USER, params);
        if( result == 1 ){
            namedParameterJdbcTemplate.update(DELETE_ROLES,params);
            return true;
        }
        return false;
    }

    public boolean addOrUpdateUser(final User user) throws InternalServerException{
        boolean isAdd = true;
        try{
            final User userInDB = getUserByName(user.getUsername());
            isAdd = false;
        }catch(final ResourceNotFoundException ex){
            // User not found
        }
        return manageUser(user, isAdd);
    }

    public List<User> getUsers()
    {
        return jdbcTemplate.query(USERS_QUERY, BeanPropertyRowMapper.newInstance(User.class));
    }

    public User getUserByName(final String name) throws ResourceNotFoundException
    {
        final Map<String, Object> params = new HashMap<>();
        params.put("username", name);
        final User user;
        try{
            user = namedParameterJdbcTemplate.queryForObject( USER_QUERY, params, BeanPropertyRowMapper.newInstance(User.class) );
            final List<String> roles = namedParameterJdbcTemplate.query( USER_ROLE_QUERY, params, new RowMapper<String>(){
                public String mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    return rs.getString(1);
                }
            } );
            if (!roles.isEmpty())
            {
                user.setRoles(String.join(",", roles));
            }
        }catch(final EmptyResultDataAccessException ex){
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }

}

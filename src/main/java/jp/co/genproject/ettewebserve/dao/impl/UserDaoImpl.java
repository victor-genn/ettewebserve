package jp.co.genproject.ettewebserve.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.genproject.ettewebserve.dao.UserDao;
import jp.co.genproject.ettewebserve.dto.UserDto;
import jp.co.genproject.ettewebserve.dto.UserUpdateDto;
import jp.co.genproject.ettewebserve.entity.User;

@Repository
public class UserDaoImpl implements UserDao {
    // JDBC
    private final NamedParameterJdbcTemplate template;

    // RowMapper
    private final BeanPropertyRowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);

    // SQL文
    private static final String SQL_INSERT_USER = "INSERT INTO user_account (login_id, password, last_name, first_name, gender, email, image_path, keyword_1, keyword_2, keyword_3, role_id) VALUES (:loginId, :password, :lastName, :firstName, :gender, :email, :imagePath, :keyword1, :keyword2, :keyword3, :roleId)";
    private static final String SQL_SELECT_USER_BY_LOGINID = "SELECT * FROM user_account WHERE login_id = :loginId ORDER BY user_id";
    private static final String SQL_SELECT_USER_BY_USERID_AND_PASSWORD = "SELECT * FROM user_account WHERE login_id = :loginId and password = :password";;
    private static final String SQL_UPDATE_USER_BY_USERID = "UPDATE user_account SET password = :password, email = :email, keyword_1 = :keyword1, keyword_2 = :keyword2, keyword_3 = :keyword3, image_path = :imagePath WHERE user_id = :userId";

    // コンストラクター
    public UserDaoImpl (NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /** 新規ユーザー登録 */ 
    public void insertUser(UserDto userDto){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("loginId", userDto.getLoginId());
        param.addValue("password", userDto.getPassword());
        param.addValue("lastName", userDto.getLastName());
        param.addValue("firstName", userDto.getFirstName());
        param.addValue("gender", userDto.getGender());
        param.addValue("email", userDto.getEmail());
        param.addValue("imagePath", userDto.getImagePath());
        param.addValue("keyword1", userDto.getKeyword1());
        param.addValue("keyword2", userDto.getKeyword2());
        param.addValue("keyword3", userDto.getKeyword3());
        param.addValue("roleId", userDto.getRoleId());
        template.update(SQL_INSERT_USER, param);
    }

    // ログインIDとしてユーザー情報取得
    public User findByloginId(String loginId){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("loginId", loginId);
        List<User> userList = template.query(SQL_SELECT_USER_BY_LOGINID, param, userRowMapper);
        return userList.isEmpty() ? null : userList.get(0);
    }

    /** ログインIDおよびパスワードとしてユーザーデータと比較 */
    public boolean signIn(String loginId, String password){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("loginId", loginId);
        param.addValue("password", password);
        List<User> userList = template.query(SQL_SELECT_USER_BY_USERID_AND_PASSWORD, param, userRowMapper);
        return !userList.isEmpty();
    }

    /** ユーザー情報更新 */
    public void updateUser(UserUpdateDto userUpdateDto) {
        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("userId", userUpdateDto.getUserId())
            .addValue("password", userUpdateDto.getPassword())
            .addValue("email", userUpdateDto.getEmail())
            .addValue("keyword1", userUpdateDto.getKeyword1())
            .addValue("keyword2", userUpdateDto.getKeyword2())
            .addValue("keyword3", userUpdateDto.getKeyword3())
            .addValue("imagePath", userUpdateDto.getImagePath());

        template.update(SQL_UPDATE_USER_BY_USERID, param);

        System.out.println("=== updateUser 실행됨 ===");
        System.out.println("userId: " + userUpdateDto.getUserId());
        System.out.println("email: " + userUpdateDto.getEmail());
    }
}

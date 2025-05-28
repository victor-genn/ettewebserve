package jp.co.genproject.ettewebserve.service;

import jp.co.genproject.ettewebserve.dto.UserDto;
import jp.co.genproject.ettewebserve.entity.User;

public interface UserService {
    
    /** 新規ユーザー登録 */
    void insertUser(UserDto userDto);

    /** ログインIDとしてユーザー情報取得 */ 
    User findByloginId(String loginId);

    /** ログインIDおよびパスワードとしてユーザーデータと比較 */
    boolean signIn(String loginId, String password);
}

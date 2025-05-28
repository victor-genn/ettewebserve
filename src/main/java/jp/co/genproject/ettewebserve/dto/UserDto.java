package jp.co.genproject.ettewebserve.dto;

public class UserDto {
    private Integer userId;
    private String loginId;
    private String password;
    private String lastName;
    private String firstName;
    private String gender;
    private String email;
    private String imagePath;
    private Integer keyword1;
    private Integer keyword2;
    private Integer keyword3;
    private Integer roleId;

    public UserDto() {
    }

    public UserDto(String loginId, String password, String lastName, String firstName,
                String gender, String email, String imagePath, Integer keyword1, Integer keyword2,
                Integer keyword3, Integer roleId) {
        this.loginId = loginId;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.email = email;
        this.imagePath = imagePath;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.roleId = roleId;
    }

    public UserDto(Integer userId, String loginId, String password, String lastName, String firstName,
                String gender, String email, String imagePath, Integer keyword1, Integer keyword2,
                Integer keyword3, Integer roleId) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.email = email;
        this.imagePath = imagePath;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(Integer keyword1) {
        this.keyword1 = keyword1;
    }

    public Integer getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(Integer keyword2) {
        this.keyword2 = keyword2;
    }

    public Integer getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(Integer keyword3) {
        this.keyword3 = keyword3;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
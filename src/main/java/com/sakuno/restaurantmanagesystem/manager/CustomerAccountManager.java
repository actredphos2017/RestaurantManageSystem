package com.sakuno.restaurantmanagesystem.manager;

import com.sakuno.restaurantmanagesystem.dataclasses.customer.CustomerFullInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.customer.CustomerLoginInfo;
import com.sakuno.restaurantmanagesystem.dataclasses.customer.CustomerRegisterInfo;
import com.sakuno.restaurantmanagesystem.utils.DatabaseRepository;
import com.sakuno.restaurantmanagesystem.utils.StateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Arrays;

@Service
public class CustomerAccountManager {

    @Autowired
    private DatabaseRepository repository;

    public boolean register(CustomerRegisterInfo info, PrintStream errorOs) {

        var username = info.getUsername();
        var password = info.getPassword();
        var phone = info.getPhone();

        if (!Character.isLetter(username.charAt(0)))
            return false;

        if (phone == null)
            phone = "";

        var id = (phone + username + System.currentTimeMillis()).hashCode();

        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用 2001");
            return false;
        }

        var prepareState = StateBuilder.Companion
                .insert()
                .into("CustomerAccounts")
                .forColumns("ID", "Username", "Password", "Phone")
                .withItems(id, username, password, phone)
                .build();

        return repository.runStatement(prepareState, errorOs);
    }

    public String login(CustomerLoginInfo info, PrintStream errorOs) {

        var account = info.getAccount();
        var password = info.getPassword();

        String accountType;
        var firstChar = account.charAt(0);
        if (Character.isLetter(firstChar)) accountType = "Username";
        else if (Character.isDigit(firstChar)) accountType = "Phone";
        else {
            errorOs.println("账号不合规 2003");
            return null;
        }

        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用 2004");
            return null;
        }

        var prepareState = StateBuilder.Companion
                .select()
                .from("CustomerAccount")
                .withCondition(accountType, account)
                .withCondition("Password", password)
                .build();

        var result = repository.runStatementWithQuery(prepareState, errorOs);
        if (result == null) {
            errorOs.println("数据库查询错误 2005");
            return null;
        }

        try {
            if (result.next()) {
                String prepareAuthCode;
                try {
                    var md5Builder = MessageDigest.getInstance("MD5");
                    prepareAuthCode = Arrays.toString(md5Builder.digest((account + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8)));
                } catch (Exception e) {
                    errorOs.println("加密出错 2002");
                    return null;
                }
                if (repository.runStatement(StateBuilder.Companion
                        .update()
                        .fromTable("CustomerAccounts")
                        .withCondition(accountType, account)
                        .change("SessionAuthCode", prepareAuthCode)
                        .build(),
                        errorOs
                )) return prepareAuthCode;
                else {
                    errorOs.println("认证码更新失败 2006");
                    return null;
                }
            }
        } catch (Exception ignore) {
            errorOs.println("数据库查询错误 2007");
            return null;
        }
        return null;
    }

    public CustomerFullInfo checkAuth(String authCode, PrintStream errorOs) {

        if (!repository.isAvailable()) {
            errorOs.println("数据库不可用 2008");
            return null;
        }

        var result = repository.runStatementWithQuery(
                StateBuilder.Companion
                        .select()
                        .from("CustomerAccounts")
                        .forColumns("ID", "Username", "Phone")
                        .withCondition("SessionAuthCode", authCode)
                        .build(),
                errorOs
        );
        try {
            if (result.next()) {
                return new CustomerFullInfo(
                        result.getString(1),
                        result.getString(3),
                        authCode,
                        result.getString(2)
                );
            } else return null;
        } catch (SQLException e) {
            errorOs.println("数据库查询错误 2009");
            return null;
        }
    }
}

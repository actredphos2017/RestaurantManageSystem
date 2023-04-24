package com.sakuno.restaurantmanagesystem;

import com.sakuno.restaurantmanagesystem.utils.DatabaseEntrance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantManageSystemApplication {

    public static void main(String[] args) {
        try {
            DatabaseEntrance.init(
                    Globals.databaseDriver,
                    Globals.databaseUrl,
                    Globals.databaseUserName,
                    Globals.databasePassword,
                    Globals.databaseName
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(RestaurantManageSystemApplication.class, args);
    }

}

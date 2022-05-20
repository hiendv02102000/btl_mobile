package com.example.project_my_app.graphql;

import androidx.annotation.NonNull;

import com.example.project_my_app.model.User;

public class Query {
    public static String loginQuery(@NonNull User user){
        String userArg =  "                user:{" +
                "                    username:\""+user.getUsername()+"\"," +
                "                    password:\""+user.getPassword()+"\"" +
                "                }";
        return "query{" +
                "        login(" +
                                    userArg+
                "            )" +
                "        {" +
                "            token" +
                "            role" +
                "        } " +
                "    }";
    }

    public  static  String signUpQuery(@NonNull User user) {
        String userArg =  "                user:{\n" +
                "                    username:\""+user.getUsername()+"\",\n" +
                "                    password:\""+user.getPassword()+"\"\n" +
                "                    first_name:\""+user.getFirstName()+"\"\n" +
                "                    last_name:\""+user.getLastName()+"\"\n" +
                "                    email:\""+user.getEmail()+"\"\n" +
                "                })\n";
        return "mutation{\n" +
                "        register_user(\n" +
                userArg+
                "         {\n" +
                "            username\n" +
                "            first_name\n" +
                "            last_name\n" +
                "            role\n" +
                "            created_at\n" +
                "        } \n" +
                "    }\n";
    }
}

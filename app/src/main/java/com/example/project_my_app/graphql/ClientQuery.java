package com.example.project_my_app.graphql;

import com.example.project_my_app.model.Song;
import com.example.project_my_app.model.User;

public class ClientQuery {
    public  static String getUserProfileQuery(){
        return "query{\n" +
                "    get_user_profile\n" +
                "    {\n" +
                "            username\n" +
                "            first_name\n" +
                "            last_name\n" +
                "            role\n" +
                "            created_at\n" +
                "            email\n" +
                "            id\n" +
                "            avatar_url\n" +
                "    }\n" +
                "}\n";
    }
    public  static String changeProfileMutation(User user){
        String userArg =  "                user:{" +
                "                    first_name:\""+user.getFirstName()+"\"," +
                "                    last_name:\""+user.getLastName()+"\"" +
                "                }";
        return "mutation{\n" +
                "          update_user_profile(\n" +
                userArg +
                ")\n" +
                "         {\n" +
                "            first_name\n" +
                "            last_name\n" +
                "            avatar_url\n" +
                "        } \n" +
                "    }\n";
    }
    public  static String changePasswordMutation(String oldPass,String newPass){
        return "mutation{\n" +
                "        change_password(\n" +
                "                user:{\n" +
                "                    old_password: \""+oldPass+"\"\n" +
                "                    password:\""+newPass+"\"\n" +
                "                })\n" +
                "        {\n" +
                "            token\n" +
                "        } \n" +
                "    }\n";
    }
    public static String getMusicList(Song music, int pageNum, int pageSize){
        String title = "";
        String singer = "";
        if(music !=null){
            if (music.getTitle()!=null){
                title = music.getTitle();
            }
            if (music.getSinger()!=null){
                singer = music.getSinger();
            }
        }

        String songArg = "                song:{\n" +
                "                    title :\""+title+"\"\n" +
                "                    singer:\""+singer+"\"\n" +
                "                }\n" ;
        String pageArg =  "                page:{\n" +
                "                    page_num:"+pageNum+"\n" +
                "                    page_size:"+pageSize+"\n" +
                "                }\n" ;
        return "query{\n" +
                "        get_song_list(\n" +
                    songArg+
                    pageArg+
                "            )\n" +
                "         {\n" +
                "             songs {\n" +
                "                    id\n" +
                "                    title\n" +
                "                    content_url\n" +
                "                    image_url\n" +
                "                    decription\n" +
                "                    created_at\n" +
                "                    singer\n" +
                "                    view\n" +
                "             }\n" +
                "        } \n" +
                "    }\n";
    }
    public static String getMusicListOfUser(User user){
        String songArg = "                song:{\n" +
                "                    user_id :"+user.getId()+"\n" +
                "                }\n" ;
        String pageArg =  "                page:{\n" +
                "                    page_num:"+1+"\n" +
                "                    page_size:"+100+"\n" +
                "                }\n" ;
        return "query{\n" +
                "        get_song_list(\n" +
                songArg+
                pageArg+
                "            )\n" +
                "         {\n" +
                "             songs {\n" +
                "                    id\n" +
                "                    title\n" +
                "                    content_url\n" +
                "                    image_url\n" +
                "                    decription\n" +
                "                    created_at\n" +
                "                    singer\n" +
                "                    view\n" +
                "             }\n" +
                "        } \n" +
                "    }\n";
    }
    public static String getDetailSong(int songId){
        return "query{\n" +
                "        get_song_detail(\n" +
                "                song:{\n" +
                "                    id:"+songId+"\n" +
                "                }\n" +
                "            )\n" +
                "         {\n" +
                "             \n" +
                "                    id\n" +
                "                    title\n" +
                "                    content_url\n" +
                "                    image_url\n" +
                "                    decription\n" +
                "                    created_at\n" +
                "                    user {\n" +
                "                        username\n" +
                "                        first_name\n" +
                "                        last_name\n" +
                "                        avatar_url\n" +
                "                    }\n" +
                "        } \n" +
                "    }\n";
    }
    public  static String createSongMutation(Song song){
        return "mutation{\n" +
                "        create_song(\n" +
                "                song:{\n" +
                "                   title: \""+song.getTitle()+"\"\n" +
                "                   decription: \""+song.getDescription()+"\"\n" +
                "                    singer:\""+song.getSinger()+"\"\n" +
                "                }\n" +
                "            )\n" +
                "         {\n" +
                "           id\n" +
                "           title\n" +
                "           content_url\n" +
                "           image_url\n" +
                "           decription\n" +
                "           created_at\n" +
                "           singer\n" +
                "        } \n" +
                "    }\n";
    }

}

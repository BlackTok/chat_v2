package commands;

public class Command {
    public static final String END = "/end";
    public static final String AUTH = "/auth";
    public static final String AUTH_OK = "/authok";

    public static final String PRIVATE_MSG = "/w";

    public static final String ERROR_MSG_UNKNOWN_USER = "Такого пользователя не существует";
    public static final String ERROR_MSG_SEND_YOURSELF = "Вы пытаетесь отправить сообщение самому себе";
}

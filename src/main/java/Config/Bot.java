package Config;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    //создаем две константы, присваиваем им значения токена и имя бота соответсвтенно
    //вместо звездочек подставляйте свои данные
    final private String BOT_TOKEN = "5612157031:AAFgBo7QHN3UE4Bb_E86E06JLfH2ndqt6u8";
    final private String BOT_NAME = "Citatybigpeople_bot";
    Storage storage;
    private ReplyKeyboardMarkup replyKeyboardMarkup;

    public Bot()
    {
        storage = new Storage();
        initKeyboard();
    }

    void initKeyboard()
    {
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); //скрываем после использования

        //Создаем список с рядами кнопок
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        //Создаем один ряд кнопок и добавляем его в список
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRow.add(new KeyboardButton("Старт"));
        //Добавляем одну кнопку с текстом "Просвяти" наш ряд
        keyboardRow.add(new KeyboardButton("Просвяти"));
        //добавляем лист с одним рядом кнопок в главный объект
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Извлекаем из объекта сообщение пользователя
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                //Достаем id чата пользователя
                String chatId = message.getChatId().toString();
                String text = message.getText();
                //Получаем текст сообщения пользователя, отправляем в написанный обработчик
                String response = parseMessage(text);

                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage sendMessage = new SendMessage();
                //Добавляем в наше сообщение id чата а также наш ответ
                sendMessage.setChatId(chatId);
                sendMessage.setText(response);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);

                //Отправка в чат
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String parseMessage(String textMsg) {
        String response;

        //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
        if (textMsg.equals("/start") || textMsg.equals("Старт")) {
            response = "Привіт! Я бот, який знає багато цитат. Натисни /get, щоб отримати випадкову цитату. Також можна скористатися кнопками нижче.";
        } else if (textMsg.equals("/get") || textMsg.equals("Просвяти")) {
            response = storage.getRandQuote();
        } else {
            response = "Повідомлення не розпізнано.";
        }
        return response;
    }
}
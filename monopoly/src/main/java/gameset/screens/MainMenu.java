package gameset.screens;

import java.io.IOException;

public class MainMenu extends AbstractScreen {
    public MainMenu() {}

    @Override
    protected void draw() {

    }

    @Override
    protected void handleInput() {
        Thread thread = new Thread(() -> {
            while (true) {
                int ch = 0;
                try {
                    ch = System.in.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                switch (ch) {
                    case '1':
                        // TODO: add constructor for next screen (getplayeramountscreen and new game)
                        setFinished(true);
                        break;
                    case '2':
                        // TODO: implement the continue game option
                        setFinished(true);
                        break;
                    case '3':
                        System.exit(0);
                        break;
                }
            }
        });
        thread.start();
    }


    //
//
//    private Scanner userInput = new Scanner(System.in);
//    public String MenuScreen() {
//        System.out.println("WELCOME TO MONOPOLY");
//        System.out.println("****************");
//        System.out.println("1. Start Game");
//        System.out.println("2. Continue Game");
//        System.out.println("3. End");
//        System.out.println("****************");
//        System.out.println("Enter the number choice: ");
//        String inputChoice = userInput.next();
//        while (!inputChoice.equals("1") && !inputChoice.equals("2") && !inputChoice.equals("3")){
//            System.out.println("Enter a valid choice");
//            inputChoice = userInput.next();
//        }
//        return inputChoice;
//    }
//
//    public int getPlayerAmountScreen() {
//        System.out.println("****************");
//        System.out.println("Getting ready to start the game");
//        System.out.println("Enter the amount of players playing: ");
//        System.out.println("****************");
//        while (true){
//            try {
//                return Integer.parseInt(userInput.next());
//            } catch (NumberFormatException ex) {
//                System.out.println("Enter an integer amount: ");
//            }
//        }
//    }
}

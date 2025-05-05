package src.network;

public class Main{
    public static void main(String[] args) {
        Server server = new Server();
        Client client = new Client();

        (new Thread() {
            public void run() {
                try {
                    System.out.println("Server");

                    server.start(1234);
                } catch(Exception v) {
                    System.out.println(v);
                }
            }  
        }).start();

        (new Thread() {
            public void run() {
                try {
                    System.out.println("Client");
                    client.start();
                } catch(Exception v) {
                    System.out.println(v);
                }
            }
        }).start();
    }
}
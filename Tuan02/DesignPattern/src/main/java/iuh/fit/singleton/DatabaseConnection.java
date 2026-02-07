package iuh.fit.singleton;

public class DatabaseConnection {
    // Volatile keyword ensures that multiple threads handle the unique instance variable correctly
    private static volatile DatabaseConnection instance;

    private boolean isConnected;
    private final int connectionId;

    // Private constructor prevents instantiation from other classes
    private DatabaseConnection() {
        this.connectionId = (int) (Math.random() * 1000);
        System.out.println("Initializing Database Connection... (Instance ID: " + connectionId + ")");
        // Simulate heavy initialization
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.isConnected = false;
    }

    public static DatabaseConnection getInstance() {
        // Double-checked locking
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public void connect() {
        if (!isConnected) {
            System.out.println("Database connected. (Instance ID: " + connectionId + ")");
            isConnected = true;
        } else {
            System.out.println("Already connected. (Instance ID: " + connectionId + ")");
        }
    }

    public void disconnect() {
        if (isConnected) {
            System.out.println("Database disconnected. (Instance ID: " + connectionId + ")");
            isConnected = false;
        } else {
            System.out.println("Already disconnected. (Instance ID: " + connectionId + ")");
        }
    }

    public void query(String sql) {
        if (isConnected) {
            System.out.println("Executing query '" + sql + "' on Instance ID: " + connectionId);
        } else {
            System.out.println("Cannot execute query. Database is not connected!");
        }
    }
}

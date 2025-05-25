import java.util.*;
import java.time.LocalDateTime;

// Class representing a stock
class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    void display() {
        System.out.printf("%-10s %-20s $%.2f\n", symbol, name, price);
    }
}

// Class representing a stock transaction
class Transaction {
    String stockSymbol;
    int quantity;
    double price;
    String type;
    LocalDateTime date;

    Transaction(String stockSymbol, int quantity, double price, String type) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.date = LocalDateTime.now();
    }

    void display() {
        System.out.printf("%-5s %5d @ $%.2f [%s] %s\n", stockSymbol, quantity, price, type, date);
    }
}

// Class representing the user
class User {
    String username;
    double balance;
    Map<String, Integer> portfolio = new HashMap<>();
    List<Transaction> transactions = new ArrayList<>();

    User(String username, double balance) {
        this.username = username;
        this.balance = balance;
    }

    void buyStock(Stock stock, int quantity) {
        double totalCost = stock.price * quantity;
        if (balance >= totalCost) {
            balance -= totalCost;
            portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
            transactions.add(new Transaction(stock.symbol, quantity, stock.price, "BUY"));
            System.out.println("Stock purchased successfully!");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);
        if (owned >= quantity) {
            balance += stock.price * quantity;
            portfolio.put(stock.symbol, owned - quantity);
            if (portfolio.get(stock.symbol) == 0) {
                portfolio.remove(stock.symbol);
            }
            transactions.add(new Transaction(stock.symbol, quantity, stock.price, "SELL"));
            System.out.println("Stock sold successfully!");
        } else {
            System.out.println("You do not own enough shares.");
        }
    }

    void viewPortfolio() {
        System.out.println("\nYour Portfolio:");
        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned.");
            return;
        }
        portfolio.forEach((symbol, qty) -> System.out.println(symbol + ": " + qty + " shares"));
        System.out.printf("Balance: $%.2f\n", balance);
    }

    void viewTransactions() {
        System.out.println("\nTransaction History:");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : transactions) {
            t.display();
        }
    }
}

// Market class
class Market {
    List<Stock> stocks = new ArrayList<>();

    Market() {
        stocks.add(new Stock("AAPL", "Apple Inc.", 175.20));
        stocks.add(new Stock("GOOGL", "Alphabet Inc.", 2820.10));
        stocks.add(new Stock("MSFT", "Microsoft Corp.", 348.15));
        stocks.add(new Stock("TSLA", "Tesla Inc.", 920.00));
    }

    void displayStocks() {
        System.out.println("\nAvailable Stocks:");
        for (Stock s : stocks) {
            s.display();
        }
    }

    Stock getStock(String symbol) {
        for (Stock s : stocks) {
            if (s.symbol.equalsIgnoreCase(symbol)) return s;
        }
        return null;
    }
}

// Main application
public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Market market = new Market();
        User user = new User("Hasini", 10000.00);

        int choice;
        do {
            System.out.println("\n=== Stock Trading Platform ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    market.displayStocks();
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = sc.next();
                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt();
                    Stock buyStock = market.getStock(buySymbol);
                    if (buyStock != null) {
                        user.buyStock(buyStock, buyQty);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = sc.next();
                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt();
                    Stock sellStock = market.getStock(sellSymbol);
                    if (sellStock != null) {
                        user.sellStock(sellStock, sellQty);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;
                case 4:
                    user.viewPortfolio();
                    break;
                case 5:
                    user.viewTransactions();
                    break;
                case 6:
                    System.out.println("Thank you for using the Stock Trading Platform!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 6);

        sc.close();
    }
}

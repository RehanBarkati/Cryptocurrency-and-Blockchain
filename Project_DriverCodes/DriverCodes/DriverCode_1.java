package DSCoinPackage;

public class DriverCode_1 {
  public static void main(String args[]) {

    Transaction t1 = new Transaction();
    Members m1 = new Members();
    m1.UID = "1000";
    t1.Source = m1;

    Transaction t2 = new Transaction();
    Members m2 = new Members();
    m2.UID = "1001";
    t2.Source = m2;

    Transaction t3 = new Transaction();
    Members m3 = new Members();
    m3.UID = "1002";
    t3.Source = m3;
    
    int score = 0;

    TransactionQueue q = new TransactionQueue();
    
    q.AddTransactions(t1);
    
    if(q.firstTransaction.Source.UID == "1000") {
      score += 3;
    }
    else {
      System.out.println("Transaction not added to queue");

    }

    q.AddTransactions(t2);

    q.AddTransactions(t3);

    try{
      Transaction t = q.RemoveTransaction();
      if(t.Source.UID == "1000") {
        score += 3;
      }
      else {
        System.out.println("Transaction(s) not removed from queue");
      }
    } catch (Exception e) {
    }

    if(q.size()==2) {
      score += 4;
    }
    else {
      System.out.println("Size of queue must be 2");
    }
    System.out.println("Final Score:");
    System.out.println(score);
  }
}

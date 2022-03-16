package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
         
        // Transaction t=new Transaction();
        transaction.next=null;

         
        if(this.firstTransaction==null){
               
              
               this.firstTransaction=this.lastTransaction=transaction;
               this.numTransactions++ ;
        }
        else{
            this.lastTransaction.next=transaction;
            this.lastTransaction=transaction;
            this.numTransactions++;
            
        }

  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
      
    
    if(this.numTransactions==0){
      throw new EmptyQueueException();
    }
    else{
           Transaction remove= this.firstTransaction;
      
             this.numTransactions-- ;
             this.firstTransaction=this.firstTransaction.next;
             remove.next=null;
             return remove;
         }
     
  }

  public int size() {
    return this.numTransactions;
  }
}

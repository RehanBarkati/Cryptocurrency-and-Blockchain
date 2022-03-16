package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
      CRF o=new CRF(64);
      int k=1000000001;

      if(this.lastBlock==null){
         while(true){
           String s= o.Fn(start_string+"#"+newBlock.trsummary+"#"+k);
           if(s.substring(0, 4).equalsIgnoreCase("0000")){
                break;
           }
            else{
              k++;
             }
         }
         newBlock.nonce=Integer.toString(k);
         newBlock.dgst=o.Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
         newBlock.previous=null;
         this.lastBlock=newBlock;

    }
    
    else{
      while(true){
        
        String s= o.Fn(this.lastBlock.dgst+"#"+newBlock.trsummary+"#"+k);
        if(s.substring(0, 4).equalsIgnoreCase("0000")){
             break;
        }
         else{
           k++;
          }
      }
   
      newBlock.nonce=Integer.toString(k);
      newBlock.dgst=o.Fn(this.lastBlock.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      newBlock.previous=this.lastBlock;
      this.lastBlock=newBlock;
    }
  }
}

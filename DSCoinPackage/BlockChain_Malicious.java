package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList=new TransactionBlock[100];
   

  public static boolean checkTransactionBlock (TransactionBlock tB) {
       CRF o=new CRF(64);
    
    
    if(tB.trsummary.compareTo(tB.Tree.Build(tB.trarray))!=0){
        return false;
    }
    if(!tB.dgst.substring(0, 4).equalsIgnoreCase("0000")){
      return false;
    }

    if(tB.previous==null){
       if(o.Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce).compareTo(tB.dgst)!=0){
           return false;
       }
    }
    else{
      if(o.Fn(tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce).compareTo(tB.dgst)!=0){
        return false;
       }
    }
    int count=0;
    for(int j=0;j<tB.trarray.length;j++){
         count=0;
       for(int k=j;k<tB.trarray.length;k++){
         if(tB.trarray[k].coinID.equals(tB.trarray[j].coinID)){
           count++;
         }
       }
       if(count>1){
         return false;
       }
    }
    if(count>1){
       return false;
    }
    //in order to avoid flag condition getting true call on previous of present block since present will contain anyway this transcation
    // only confusion if more than one coin is present with same ID,then return false or not to check that condition
    if(tB.previous!=null){
    for(int i=0;i<tB.trarray.length;i++){
          if(!tB.previous.checkTransaction(tB.trarray[i])){
              return false;
          }
    }
  }
    return true;
  }

  public TransactionBlock FindLongestValidChain () {

       int index=0;
       int countmax=0;
       int i=0;
       while(this.lastBlocksList[i]!=null){
          TransactionBlock b=this.lastBlocksList[i];
          int count=0;
          while(b!=null){
            if(checkTransactionBlock(b)){
              count++;
              b=b.previous;
            }
            else{
              count=0;
              b=b.previous;
            }
          }
          if(count>=countmax){
            index=i;
      //  TransactionBlock last=this.lastBlocksList[index];
            countmax=count;
          }
          i++;
       }
      
       TransactionBlock last=this.lastBlocksList[index];
       TransactionBlock tmp=this.lastBlocksList[index];

       while(tmp!=null){
           if(checkTransactionBlock(tmp)){
                tmp=tmp.previous;
           }
           else{
             tmp=tmp.previous;
             last=tmp;
           }
       }

    return last;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    CRF o=new CRF(64);
    int k=1000000001;
    //what if there is no lastblock in list
    if(this.lastBlocksList[0]==null){
           this.lastBlocksList[0]=newBlock;
           
           while(true){
            String s= o.Fn("DSCoin"+"#"+newBlock.trsummary+"#"+k);
            if(s.substring(0, 4).equalsIgnoreCase("0000")){
                 break;
            }
             else{
               k++;
              }
            }

            newBlock.nonce=Integer.toString(k);
            newBlock.dgst=o.Fn("DSCoin"+"#"+newBlock.trsummary+"#"+newBlock.nonce);
            newBlock.previous=null;

    }
    else{
       TransactionBlock b= this.FindLongestValidChain();
      
      while(true){
        String s= o.Fn(b.dgst+"#"+newBlock.trsummary+"#"+k);
        if(s.substring(0, 4).equalsIgnoreCase("0000")){
             break;
        }
         else{
           k++;
          }
      }
   
      newBlock.nonce=Integer.toString(k);
      newBlock.dgst=o.Fn(b.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      newBlock.previous=b;

      int i=0;
      while(this.lastBlocksList[i]!=null && this.lastBlocksList[i]!=b){
               i++;
      }
      this.lastBlocksList[i]=newBlock;
    }   
  }
}

package DSCoinPackage;

import DSCoinPackage.*;
import HelperClasses.*;
import java.util.*;

public class DriverCode_9 {
    public static void main(String args[])
    {

        int num_of_members=3, tr_count=4;
        int coinCount = num_of_members * tr_count;
        String initial_member_id = "1000", initial_coin_id  = "100000";
        int score = 0;

        // Creating moderator object

        Moderator mod = new Moderator();

        // Creating and initialising attributes of DSCoin
        DSCoin_Honest DSObj = new DSCoin_Honest();
        DSObj.pendingTransactions = new TransactionQueue();
        DSObj.bChain = new BlockChain_Honest();

        DSObj.bChain.tr_count = tr_count;
        DSObj.memberlist = new Members[num_of_members];
        DSObj.latestCoinID = initial_coin_id;

        // Initialising attributes of members of DSCoin
        for(int i=0;i<num_of_members;i++)
        {
            Members m = new Members();
            m.UID = Long.toString(Long.parseLong(initial_member_id) + i);
            m.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
            m.in_process_trans = new Transaction[100];
            DSObj.memberlist[i] = m;
        }

        // Finding expected list of coins alloted to each member by moderator
        ArrayList<ArrayList<String>> coin_list = new ArrayList<ArrayList<String>>();

        for(int i=0;i<num_of_members;i++)
        {
          ArrayList<String> myCoinList = new ArrayList<String>();

          for(int j=0;j<tr_count;j++)
          myCoinList.add(Long.toString(Long.parseLong(initial_coin_id) + i + j*num_of_members));

          coin_list.add(myCoinList);
        }

        try
        {
            mod.initializeDSCoin(DSObj, coinCount);
            score += 1;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        
        boolean check = true;

        // Checking equality of alloted coins(expected) and mycoins of members
        for(int i=0;i<num_of_members;i++)
        {
          List<String> mycoins =  new ArrayList<String>();

          for(int j=0;j<DSObj.memberlist[i].mycoins.size();j++)
          mycoins.add(DSObj.memberlist[i].mycoins.get(j).first);

          if(mycoins.size()!=coin_list.get(i).size()) {
            System.out.printf("Coin list for member %s not matching!!\n",DSObj.memberlist[i].UID);
            continue;
          }

          boolean flag = true;
          for(int j = 0; j<mycoins.size(); j++) {
            if(!(mycoins.get(j)).equals((coin_list.get(i)).get(j)))
            {
              System.out.printf("Coin list for member %s not matching!!\n",DSObj.memberlist[i].UID);
              flag = false;
              break;
            }  
          }

          check = check & flag;

          if(!flag)
            continue;

          score += 2;

        }

        // Authenticating BlockChain
        if(check && AutheticateBlockChain(DSObj.bChain.lastBlock))
          score  += 3;   // for not auth

        // we are not checking if latestCoinID of DSCoin is changed

        System.out.println(score);

    }
    
    
    /*
    Function that accepts the lastBlock of BlockChain and returns true if all blocks are correctly computed and inserted using previous dgst, trsummary,nonce

    */
    public static Boolean AutheticateBlockChain(TransactionBlock tblock)
    {
      CRF obj = new CRF(64);
      TransactionBlock curr = tblock;
      while(curr!=null)
      {
        if(curr.previous!=null)
        {
          String hsh = obj.Fn(curr.previous.dgst + "#" + curr.trsummary + "#" + curr.nonce);
          // System.out.println(hsh + " | " + curr.dgst);
          if(!hsh.equals(curr.dgst))
          return false;
        }
        else
        {
          String hsh = obj.Fn("DSCoin" + "#" + curr.trsummary + "#" + curr.nonce);
          // System.out.println(hsh + " | " + curr.dgst);
          if(!hsh.equals(curr.dgst))
          return false;
        }

        curr = curr.previous;
      }
      return true;
    }

}

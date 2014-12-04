/*
 * Please go to https://github.com/sidewallme/FlagWall-CS to know more usages and more about us.
 * Please go to https://github.com/sidewallme/FlagWall-CS to know more usage and more about us.
 * Please go to https://github.com/sidewallme/FlagWall-CS to know more usage and more about us.
 */
package pureuiuc;
import java.util.*;
import java.lang.*;
import java.io.*;
import com.jaunt.*;
import java.sql.*;
/**
 *
 * @author Jiarui
 */
public class Pureuiuc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //for example:
        //System.out.println(parsing("http://illinois.edu/calendar/detail/2654?eventId=31392467"));
        
        String URL="http://illinois.edu/calendar/detail/2654?eventId=31392467"; //Sample: http://illinois.edu/calendar/detail/2654?eventId=31392467
        SQL(parsing(URL));
        
    }
    public static String parsing(String url){
        String[] labels=new String[20];
        String[] infos=new String[20];
        String[] metadata=new String[13];
        String thetitle="";
        String query="INSERT INTO events ";
        try{
        UserAgent userAgent = new UserAgent();
        userAgent.visit(url);
    	Elements event_label = userAgent.doc.findEvery("<span class=\"column-label\">");  
        String event_tags=event_label.outerHTML();
        //System.out.println(event_tags);
        Elements title=userAgent.doc.findEvery("<h2 class=\"detail-title\">");
        String event_title=title.outerHTML();
        //System.out.println(event_title.substring(36,event_title.length()-17));
        thetitle=event_title.substring(36,event_title.length()-17);
        
        Elements desc=userAgent.doc.findEvery("<p style=\"text-align: justify;\">");
        String event_desc=desc.outerHTML();
        //System.out.println(event_desc);
        
    	Elements event_info = userAgent.doc.findEvery("<span class=\"column-info\">");  
    	String event_infos=event_info.outerHTML();  
        
    	event_tags=event_tags.substring(11,event_tags.length()-11);
    	String[] tag_result = event_tags.split("<span class=\"column-label\">");
    	
        event_infos=event_infos.substring(11,event_infos.length()-11);
        String[] info_result = event_infos.split("<span class=\"column-info\">");
                
    	for (int x=1; x<tag_result.length; x++){
    	    tag_result[x]=tag_result[x].substring(0,tag_result[x].length()-8);
            info_result[x]=info_result[x].substring(0,info_result[x].length()-7);
    	}
    	for (int x=1; x<tag_result.length; x++){
    	    labels[x-1]=tag_result[x];
            infos[x-1]=info_result[x];
            //System.out.println(result[x]);
    	}
        for(int i=0;i<tag_result.length-1;i++){
            //System.out.println(labels[i]+" : "+infos[i]);
            if(labels[i].equals("Date"))
                metadata[1]="2014-12-12";
            else if(labels[i].equals("Location"))
                metadata[4]=infos[i];
            else if(labels[i].equals("Sponsor"))
                metadata[5]=infos[i];
            else if(labels[i].equals("Contact"))
                metadata[6]=infos[i];
            else if(labels[i].equals("E-mail"))
                metadata[7]=infos[i];
            else if(labels[i].equals("Registration"))
                metadata[9]=infos[i];
        }
        metadata[0]=thetitle;
        metadata[2]="17:00:00";
        metadata[3]="19:00:00";
        int typeId=ann(counter_result(event_desc+" "+thetitle));
        metadata[8]=types[typeId];
        if(typeId==2)
            metadata[10]="1";
        else
            metadata[10]="0";
        metadata[11]=event_desc;
        metadata[12]=url;
        String query2=" VALUES (NULL,"+
                "'"+metadata[0]+"'," +
                "'"+metadata[1]+"'," +
                "'"+metadata[2]+"'," +
                "'"+metadata[3]+"'," +
                "'"+metadata[4]+"'," +
                "'"+metadata[5]+"'," +
                "'"+metadata[6]+"'," +
                "'"+metadata[7]+"'," +
                "'"+metadata[8]+"'," +
                "'"+metadata[9]+"'," +
                "'"+metadata[10]+"'," +
                "'"+metadata[11]+"'," +
                "'"+metadata[12]+"'" +
                
                ")";
        return query+query2;        
        }
    	catch(JauntException e){
            System.err.println(e);
    	}
        //{$name}','{$date}', '{$starttime}','{$endtime}', '{$location}', 
   // '{$sponsor}', '{$contact}', '{$email}', '{$type}', '{$registration}', 
//'{$freefood}', '{$description}', '{$source}'
        return "";
    }
    
    public static void SQL(String query) {
    Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
      
      //STEP 4: Execute a query
      System.out.println("Inserting records into the table...");
      stmt = conn.createStatement();
      
      //String sql = "INSERT INTO users " +
      //             "VALUES (NULL, 'ddmy', 'Ali', 'hihi','koko','djiji@qq.com')";
      String sql=query;
      stmt.executeUpdate(sql);
      
      System.out.println("Inserted records into the table...");

   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            conn.close();
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Query finished!");
   
   }
    public static void mining(String url){
        String query="";
        try{
        UserAgent userAgent = new UserAgent();
        userAgent.visit(url);
    	Elements event_label = userAgent.doc.findEvery("<span class=\"column-label\">");  
        String event_tag=event_label.outerHTML();
    	//System.out.println(event_tag);     
    		  
    	Elements event_info = userAgent.doc.findEvery("<span class=\"column-info\">");  
    	//System.out.println(event_info.outerHTML());  
    		  
    	//System.out.println("\nTry to extract the tags out: (sample)\n");
    	event_tag=event_tag.substring(11,event_tag.length()-11);
    	String[] result = event_tag.split("<span class=\"column-label\">");
    		  
    	for (int x=1; x<result.length; x++){
    	    result[x]=result[x].substring(0,result[x].length()-8);
    	}
    	for (int x=1; x<result.length; x++){
    	    System.out.println(result[x]);
    	}
        }
    	catch(JauntException e){
            System.err.println(e);
    	}
    }
    public static int[] counter_result(String input){
        int[] result=new int[47];
        StringTokenizer st = new StringTokenizer(input,",[]{}()!// .");
        while(st.hasMoreTokens()){
            //System.out.println(st.nextToken());
            int get=hasher(st.nextToken());
            if(get==-1)continue;
            else 
                result[get]++;
        }
        return result;
    }
    public static int hasher(String input){
        String[] all={"arc","RSVP","fair","company","internship","career","resume","major","food","drink","fun","bar","Murphy's","dance","game","appetizers","club","casual","burger","drink","papa","pizza","dinner","provided","full","part","time","resume","contact","email","needed","looking","Company","background","research","professor","laboratory","programmer","paided","study","participant","paper","university","seminar","talk","presentation","phd"};
        for(int i=0;i<47;i++){
            if(input.equalsIgnoreCase(all[i])){           
                return i;
            }
        }
        return -1;
    }
    public static int ann(int[] inputs){
        System.out.println(inputs.length);
        int bias1=1;
        int bias2=1;
        double[] enters=new double[48];
        double[] hidden_in=new double[6];
        double[] hidden_out=new double[6];
        hidden_out[5]=1;
        double[] output_in=new double[6];
        double[] output_out=new double[6];
        
        for(int i=0;i<47;i++){
            enters[i]=inputs[i];
        }
        enters[47]=1;
        
        for(int i=0;i<5;i++){
            double input=0;
            for(int j=0;j<48;j++){
                input+=fweights[j][i]*enters[j];
            }
            hidden_out[i]=sigmoid(input);
        }
        for(int i=0;i<6;i++){
            double input=0;
            for(int j=0;j<6;j++){
                input+=sweights[j][i]*hidden_out[j];
            }
            output_out[i]=sigmoid(input);
        }
        double max=0;
        int index=0;
        for(int i=0;i<6;i++){
            System.out.println(output_out[i]);
            if(output_out[i]>max){
                max=output_out[i];
                index=i;
            }
        }
        return index;
    }
    public static double sigmoid(double x){
        return 1/(1+Math.pow(2.718281829,-x));
    }
    
    static double[][] fweights=new double[][]{
{-1.509899 ,0.881099 ,1.425459 ,-0.454786 ,3.741382 },
{-1.730959 ,1.064862 ,1.611337 ,-0.751519 ,4.554978 },
{-2.434996 ,1.257424 ,1.833735 ,-0.953140 ,5.188962 },
{-2.197208 ,1.680330 ,2.370227 ,-0.719440 ,6.494974 },
{-2.205481 ,1.606443 ,2.535672 ,-0.781298 ,6.534476 },
{-2.475229 ,1.841595 ,2.545604 ,-1.146893 ,6.708025 },
{-2.559181 ,1.885455 ,2.643405 ,-1.025856 ,6.897956 },
{-1.153459 ,0.865963 ,1.139430 ,-0.315723 ,3.149639 },
{-1.882240 ,1.026200 ,0.143160 ,2.758855 ,-2.171307 },
{-1.155787 ,1.114828 ,0.363159 ,2.365958 ,-1.289104 },
{-1.207588 ,0.615321 ,0.076288 ,1.866148 ,-1.421935 },
{-1.367274 ,1.555283 ,0.505506 ,2.864247 ,-1.288570 },
{-2.066655 ,1.433848 ,0.217676 ,3.493320 ,-2.310827 },
{-2.170913 ,1.350469 ,0.324513 ,3.280508 ,-2.312947 },
{-1.003854 ,0.788986 ,0.310750 ,1.968445 ,-1.126685 },
{-2.786473 ,2.055751 ,0.498607 ,4.672078 ,-2.969305 },
{-1.901463 ,1.141866 ,0.235613 ,2.986134 ,-2.013104 },
{-2.285477 ,1.561504 ,0.222507 ,3.814187 ,-2.528774 },
{1.970582 ,-0.458253 ,-2.238839 ,-1.784220 ,3.226439 },
{1.368890 ,-0.133785 ,-1.173888 ,-0.926314 ,2.137958 },
{1.553646 ,-0.147209 ,-1.351596 ,-0.999402 ,2.156646 },
{1.811943 ,-0.268946 ,-1.697877 ,-1.270516 ,2.615591 },
{2.250358 ,-0.497584 ,-2.275615 ,-1.753579 ,3.509597 },
{2.202676 ,-0.396676 ,-2.082634 ,-1.598419 ,3.384215 },
{1.703004 ,3.913500 ,-0.184794 ,-3.467304 ,-3.107984 },
{1.570899 ,3.329817 ,-0.143280 ,-2.922734 ,-2.646506 },
{1.504167 ,3.332741 ,-0.084024 ,-2.885285 ,-2.635130 },
{1.717864 ,3.884875 ,-0.194284 ,-3.738150 ,-3.310987 },
{1.560177 ,3.413365 ,-0.099110 ,-2.716996 ,-2.439265 },
{1.184678 ,2.839049 ,-0.153841 ,-2.694263 ,-2.423403 },
{1.565168 ,3.148962 ,-0.076681 ,-2.691253 ,-2.404481 },
{1.517753 ,3.541716 ,-0.144054 ,-3.096558 ,-2.847639 },
{1.957674 ,4.381772 ,-0.298107 ,-4.093104 ,-3.542395 },
{1.169026 ,2.795767 ,-0.073781 ,-2.598829 ,-2.201069 },
{1.926409 ,-0.372402 ,-2.486977 ,3.388259 ,-1.067671 },
{1.907525 ,-0.357921 ,-2.502050 ,3.274765 ,-1.103788 },
{1.673473 ,-0.246614 ,-1.994330 ,2.716002 ,-0.855301 },
{1.367328 ,-0.265730 ,-1.848665 ,2.414847 ,-0.862526 },
{1.561730 ,-0.208394 ,-2.015974 ,2.654926 ,-0.819801 },
{1.780582 ,-0.145834 ,-2.146358 ,2.971887 ,-0.850835 },
{2.289525 ,-0.640339 ,-3.002993 ,3.887464 ,-1.240150 },
{1.378911 ,-0.366304 ,-2.077512 ,2.669415 ,-0.902054 },
{-0.473897 ,-5.932436 ,3.028332 ,-0.705420 ,-0.824387 },
{-0.393469 ,-5.156325 ,2.838021 ,-0.631029 ,-0.806156 },
{-0.280373 ,-5.202938 ,2.580032 ,-0.465733 ,-0.639210 },
{-0.350855 ,-5.670795 ,2.969995 ,-0.529797 ,-0.632367 },
{-0.305967 ,-5.776496 ,2.893815 ,-0.482316 ,-0.549766 },
{1.077142 ,-1.177876 ,0.705681 ,-0.230789 ,-0.475726 },
};

    static double[][] sweights=new double[][]{
        {-5.862492 ,-6.792993 ,1.744655 ,3.204815 ,1.482445 ,-2.100763},
        {2.031704 ,2.611334 ,-3.410498 ,5.438618 ,-3.800863 ,-7.713937}, 
        {2.002889 ,0.544942 ,-5.322595 ,-2.005278 ,-5.579643 ,5.151142},
        {-4.130291 ,4.887907 ,-4.795403 ,-6.231798 ,5.001012 ,-3.714832}, 
        {5.315334 ,-4.748041 ,5.197941 ,-5.918291 ,-4.567979 ,-3.831394},
        {-4.867078 ,-3.469750 ,-2.070561 ,-3.426423 ,-1.713951 ,0.774773},
    };
    static String [] types={"Job fair",	"Entertainment","Free Food","Jobs","Research","Seminar"};
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://50.62.209.88/cs411_flagwall";
   //  Database credentials
    static final String USER = "cs411admin";
    static final String PASS = "xusuqun2008";
}

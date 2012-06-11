package com.txtweb.campaign.analysis;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.placeiq.inputformat.KeyTextInputFormat;
        
public class MobileCount {
        
public static class Map extends Mapper<Text, Text, Text, Text> {

    private Text category = new Text();
        
    public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        
	   String line = value.toString(); // Read the category of mobile number
        
        StringTokenizer tokenizer = new StringTokenizer(line); // Ideally we shouldn't have more than one token
        
        while (tokenizer.hasMoreTokens()) {

        	category.set(tokenizer.nextToken());

            context.write(key, category);

        }

    }
 } 
        
 public static class Reduce extends Reducer<Text, Text, Text, String> {

    public void reduce(Text key, Iterable<Text> values, Context context) 
      throws IOException, InterruptedException {
        
	   boolean isUnique = true;

        for (Text val : values) {

            String category = val.toString();
	
	       // Since we get all mobile numbers and the category of it, we need to ignore all numbers which are part of "S" - scrubbbed
	       if (category.equals("S")) isUnique = false;

        }

	   // Write output of mobile number only if it's unique

	   if(isUnique) {
       	 context.write(key, "");
	  }

    }
}
       
 public static void main(String[] args) throws Exception {

    Configuration conf = new Configuration();
        
    Job job = new Job(conf, "mobilecount");
   
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
        
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    //conf.set("key.value.separator.in.input.line", "/t");
       
    job.setInputFormatClass(KeyTextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
        
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
    job.waitForCompletion(true);

}
       
}

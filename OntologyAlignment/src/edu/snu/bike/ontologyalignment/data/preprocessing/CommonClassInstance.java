package edu.snu.bike.ontologyalignment.data.preprocessing;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import edu.snu.bike.ontologyalignment.util.NTParser;

/**
 * this class is for generting class-instanceï¼?e.g. class1
 * instance1,instance2,instance3
 * 
 */
public class CommonClassInstance extends Configured implements Tool {

	/**
	 * star shape file
	 * 
	 * 
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		private NTParser parser = new NTParser();

		// Map function for first iteration
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			Text s = new Text();
			Text o = new Text();

			String line = value.toString();
			String[] so = line.split("	"); // tab
			if (so.length > 1) {
				for (String obj : so[1].split(" ")) {
					s.set(so[0].trim());
					o.set(obj.trim());
					context.write(o, s);
				}
			}
		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

			Iterator itr = values.iterator();
			Text word_value = new Text();
			// ArrayList<String> triples = new ArrayList<String>();
			StringBuffer sb = new StringBuffer();

			int number = 0;

			while (itr.hasNext()) {

				String sbj = itr.next().toString();

				sb.append(sbj + " ");

				number++;
			}

			word_value.set(sb.toString().trim());

			context.write(key, word_value);

		}
	}

	@Override
	public int run(String[] args) throws Exception {

		Job job = new Job(getConf());
		job.setJarByClass(CommonClassInstance.class);
		job.setJobName("Siren Indexer");

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean success = job.waitForCompletion(true);

		return success ? 0 : 1;

	}

	public Text NullText() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		int ret = ToolRunner.run(new CommonClassInstance(), args);
		System.exit(ret);

	}
}

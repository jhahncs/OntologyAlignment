package edu.snu.bike.ontologyalignment.data.preprocessing;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
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
 * 
 * 
 */
public class CommonInstanceClass extends Configured implements Tool {

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

			if (line.startsWith("<http://") && !line.contains("# <BAD URI: Illegal character")) {
				String[] statement = parser.parse(line);

				if (statement[1].equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")) {

					s.set(statement[0]);
					o.set(statement[2]);
					context.write(s, o);

				}
			}

		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

			Configuration conf = context.getConfiguration();
			String namespace1 = conf.get("namespace1");
			String namespace2 = conf.get("namespace2");
			int oclsfreq = Integer.valueOf(conf.get("oclass"));
			int rclsfreq = Integer.valueOf(conf.get("rclass"));
			int oinsfreq = Integer.valueOf(conf.get("oinstance"));
			int rinsfreq = Integer.valueOf(conf.get("rinstance"));

			Iterator itr = values.iterator();
			Text word_value = new Text();
			StringBuffer sb = new StringBuffer();
			HashSet<String> namespaces = new HashSet<String>();

			int number = 0;
			while (itr.hasNext()) {

				String o = itr.next().toString();

				if (o.startsWith(namespace1)) {
					namespaces.add(namespace1);
				}
				if (o.startsWith(namespace2)) {
					namespaces.add(namespace2);
				}

				sb.append(o + " ");

				number++;
			}

			if (namespaces.size() == 2) {
				oclsfreq += number;
				rclsfreq += number;
				oinsfreq++;
				rinsfreq++;

				conf.set("oclass", String.valueOf(oclsfreq));
				conf.set("rclass", String.valueOf(rclsfreq));
				conf.set("oinstance", String.valueOf(oinsfreq));
				conf.set("rinstance", String.valueOf(rinsfreq));
				word_value.set(sb.toString().trim());
				context.write(key, word_value);

			} else {
				oclsfreq += number;
				oinsfreq++;
				conf.set("oclass", String.valueOf(oclsfreq));
				conf.set("oinstance", String.valueOf(oinsfreq));
			}

		}
	}

	@Override
	public int run(String[] args) throws Exception {

		Job job = new Job(getConf());
		job.setJarByClass(CommonInstanceClass.class);
		job.setJobName("Siren Indexer");

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.getConfiguration().set("namespace1", "");
		job.getConfiguration().set("namespace2", "");
		job.getConfiguration().set("oclass", "0");
		job.getConfiguration().set("rclass", "0");
		job.getConfiguration().set("oinstance", "0");
		job.getConfiguration().set("rinstance", "0");

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean success = job.waitForCompletion(true);

		int oclass = Integer.valueOf(job.getConfiguration().get("oclass"));
		int rclass = Integer.valueOf(job.getConfiguration().get("rclass"));
		int oinstance = Integer.valueOf(job.getConfiguration().get("oinstance"));
		int rinstance = Integer.valueOf(job.getConfiguration().get("rinstance"));

		System.out.println("# origial class: " + oclass);
		System.out.println("# left class: " + rclass);
		System.out.println("# origial instance: " + oinstance);
		System.out.println("# left instance: " + rinstance);

		return success ? 0 : 1;

	}

	public Text NullText() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		int ret = ToolRunner.run(new CommonInstanceClass(), args);
		System.exit(ret);

	}
}

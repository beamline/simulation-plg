package beamline.tester;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.deckfour.xes.model.XLog;

import beamline.events.BEvent;
import beamline.sources.XesLogSource;
import plg.generator.ProgressAdapter;
import plg.generator.log.LogGenerator;
import plg.generator.log.SimulationConfiguration;
import plg.generator.process.ProcessGenerator;
import plg.generator.process.RandomizationConfiguration;
import plg.model.Process;

public class Tester {

	public static void main(String[] args) throws Exception {
		
		Process p = new Process("");
		ProcessGenerator.randomizeProcess(p, RandomizationConfiguration.BASIC_VALUES);
		
		LogGenerator logGenerator = new LogGenerator(p, new SimulationConfiguration(100), new ProgressAdapter());
		XLog log = logGenerator.generateLog();
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env
			.addSource(new XesLogSource(log))
			.keyBy(BEvent::getProcessName)
			.print();
		env.execute();
	}
}

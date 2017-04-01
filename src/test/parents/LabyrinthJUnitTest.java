package test.parents;

import java.util.Random;

import org.mockito.Mockito;

import com.google.gson.Gson;
import com.labels.LabyrinthMessages;

import test.utils.RandomStrings;

public class LabyrinthJUnitTest extends Mockito
{
	protected Random rand = new Random();
	protected Gson gson = new Gson();
	protected LabyrinthMessages messages = new LabyrinthMessages();
	protected RandomStrings randStrings = new RandomStrings();
}

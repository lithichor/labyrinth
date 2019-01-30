package test.parents;

import java.util.Random;

import org.mockito.Mockito;

import com.labels.LabyrinthMessages;
import com.models.wrappers.GsonWrapper;

import test.utils.RandomStrings;

public class LabyrinthJUnitTest extends Mockito
{
	protected Random rand = new Random();
	protected GsonWrapper gson = new GsonWrapper();
	protected LabyrinthMessages messages = new LabyrinthMessages();
	protected RandomStrings randStrings = new RandomStrings();
}

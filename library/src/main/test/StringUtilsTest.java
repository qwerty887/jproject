import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtilsTest {

    @Test
    public void testSum() {
        String s = "(?i:jpeg)|(?i:skp)";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher("skp");

        System.out.println(matcher.matches());
        System.out.println(matcher.find());
        System.out.println(matcher.groupCount());
    }

    @Test
    public void test2() throws ScriptException {
        Object attrValue = "C:\\Users\\Рустам\\Downloads\\Fallout.S02.2160p.AMZN.WEB-DL.SDR.H.265\\Fallout.S02E07.2160p.AMZN.WEB-DL.H.265.RGzsRutracker.mkv";
        Object targetValue = "C:\\Users\\Рустам\\Downloads\\Fallout.S02.2160p.AMZN.WEB-DL.SDR.H.265\\Fallout.S02E07.2160p.AMZN.WEB-DL.H.265.RGzsRutracker.mkv";
        System.out.println(attrValue.equals(targetValue));
    }

    @Test
    public void json() throws JsonProcessingException {
        /*
        DtoFileGroupingProcessParam fileGroupingParam = new DtoFileGroupingProcessParam(123123);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(fileGroupingParam);

        System.out.println(jsonString);
        */

        for (int i = 0; i < 5; i++) {
            System.out.println(i); // Prints 0 to 4
        }
    }

    /*
    @Test
    public void json2() throws JsonProcessingException {
        String json = "{\"processType\":\"FILE_FETCHING\",\"path\":\"file:///C:/Users/Рустам/Downloads/Fallout.S02.WEB-DLRip.LF/\"}";
        DtoFileFetchingProcessParam param = (new ObjectMapper()).readValue(json, DtoFileFetchingProcessParam.class);

        System.out.println(param.getProcessType());
        System.out.println(param.getPath());

    }
    */

}

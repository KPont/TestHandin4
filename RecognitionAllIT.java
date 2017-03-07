/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author Kasper
 */
@RunWith(Parameterized.class)
public class RecognitionAllIT {
    private static final int currentlyCorrectSnapshots = 53;
    private static final Logger logger = LoggerFactory.getLogger(RecognitionIT.class);
    private File input;
    private String expected;
    
    
    public RecognitionAllIT(File input, String expected){
        this.input = input;
        this.expected = expected;
    }
    
    @Parameterized.Parameters
    public static Collection<Object[]> plates() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException{
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        
        InputStream resultsStream = new FileInputStream(new File(resultsPath));
        Properties properties = new Properties();
        properties.load(resultsStream);
        resultsStream.close();
        
        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();
         
        Collection<Object[]> data = new ArrayList();
        
        for (File snapshot : snapshots) {
            data.add(new Object[]{snapshot, properties.getProperty(snapshot.getName())});
        }
        return data;
    }
    
    @Test
    public void testPlate() throws Exception {
        CarSnapshot carSnap = new CarSnapshot(new FileInputStream(this.input));
        Intelligence intel = new Intelligence();
//        assertEquals(intel.recognize(carSnap), expected);

        //Using hamcrest matchers
        assertThat(intel.recognize(carSnap), is(equalTo(this.expected)));
    }
}

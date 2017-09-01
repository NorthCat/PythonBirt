import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.DocxRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.model.api.SessionHandle;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.HashMap;
import java.io.*;
import java.net.URL;


public class Birt4Py {

      private static String report;
      private static IReportEngine engine = null;
      private static EngineConfig  config = null;

      public static String getReport() {
        return report;
      }

      public Birt4Py() throws BirtException {
          // start up Platform
          config = new EngineConfig( );

          // Try to load JDBC Driver
          config.getAppContext().put("OdaJDBCDriverClassPath", "/usr/lib/jvm/java-8-oracle/jre/lib/ext/postgresql-42.1.4.jar");

          Platform.startup( config );

          // create new Report Engine
          IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
          engine = factory.createReportEngine( config );
      }

      public void executeReport(ArrayList<String> params) throws EngineException {
          /*
            Params are passed through the /sirenaApp/data_helpers/report_extractor -> get_report_file() -> my_list:
                1st: .rptdesign full path -> String
                2nd: report file full path -> String
                3rd: report extension -> String

          */

          // open the report design
          IReportRunnable design = null;
          System.out.println(params);
          design = engine.openReportDesign(params.get(0));

          // create RunandRender Task
          IRunAndRenderTask task = engine.createRunAndRenderTask(design);

          // pass necessary parameters
          task.validateParameters();

          // set render options including output type
          RenderOption options;
          if (params.get(2) == "doc") {
            options = new RenderOption();
          }
          else if (params.get(2) == "xls") {
            options = new EXCELRenderOption();
          }
          else {
            options = new HTMLRenderOption();
          }

          ByteArrayOutputStream outs = new ByteArrayOutputStream();
          options.setOutputStream(outs);
          options.setImageHandler(new HTMLServerImageHandler());

          options.setOutputFileName(params.get(1));
          options.setOutputFormat(params.get(2));
          task.setRenderOption(options);

          String output;
          task.run();
          output = outs.toString();
          task.close();
          report = output;

          System.out.println(report);

          }
      }

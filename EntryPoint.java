import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;

import py4j.GatewayServer;
import java.util.ArrayList;
import java.util.List;

public class EntryPoint {
  private Birt4Py birt;

  public EntryPoint() throws BirtException {
      birt  = new Birt4Py();

  }

  public Birt4Py getStack(ArrayList<String> params) throws EngineException {

      birt.executeReport(params);
      return birt;
  }

  public static void main(String[] args) throws BirtException {
      GatewayServer gatewayServer = new GatewayServer(new EntryPoint());
      gatewayServer.start();
      System.out.println("Birt4Py listener started!");
  }
}

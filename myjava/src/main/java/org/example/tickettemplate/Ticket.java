package org.example.tickettemplate;

import java.util.List;

public class Ticket {
    private String id;
    private String keyName;
    private List<String> admins;
    private String url;
    private String desc;
    private String homepage;
    private String httpMethod;
    private AuthInfo auth;
    private List<SiteKeyAndEndpoint> siteKeyAndEndpoints;
    private RequestParams requestParams;
    private String status;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }

    public List<String> getAdmins() { return admins; }
    public void setAdmins(List<String> admins) { this.admins = admins; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public AuthInfo getAuth() { return auth; }
    public void setAuth(AuthInfo auth) { this.auth = auth; }

    public List<SiteKeyAndEndpoint> getSiteKeyAndEndpoints() { return siteKeyAndEndpoints; }
    public void setSiteKeyAndEndpoints(List<SiteKeyAndEndpoint> siteKeyAndEndpoints) { this.siteKeyAndEndpoints = siteKeyAndEndpoints; }

    public RequestParams getRequestParams() { return requestParams; }
    public void setRequestParams(RequestParams requestParams) { this.requestParams = requestParams; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

class AuthInfo {
    private String method;
    private String token;

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

class SiteKeyAndEndpoint {
    private String siteKey;
    private String endpoint;
    private String idc;

    public String getSiteKey() { return siteKey; }
    public void setSiteKey(String siteKey) { this.siteKey = siteKey; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getIdc() { return idc; }
    public void setIdc(String idc) { this.idc = idc; }
}

class RequestParams {
    private List<Param> paramGroup;

    public List<Param> getParamGroup() { return paramGroup; }
    public void setParamGroup(List<Param> paramGroup) { this.paramGroup = paramGroup; }
}

class Param {
    private String keyName;
    private String value;
    private String displayName;
    private String elementType;
    private String structure;
    private boolean required;
    private boolean editable;
    private List<String> options;

    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getElementType() { return elementType; }
    public void setElementType(String elementType) { this.elementType = elementType; }

    public String getStructure() { return structure; }
    public void setStructure(String structure) { this.structure = structure; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public boolean isEditable() { return editable; }
    public void setEditable(boolean editable) { this.editable = editable; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
}

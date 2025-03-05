package __ticket_template

type Ticket struct {
	Id                  string               `json:"id"`          // ticket system id in ticket database
	KeyName             string               `json:"key_name"`    // unique identification of ticket system
	Admins              []string             `json:"admins"`      // the administrators of this ticket
	Url                 string               `json:"url"`         // url of this ticket system that I request to
	Desc                string               `json:"desc"`        // description of ticket system
	Homepage            string               `json:"homepage"`    // homepage of ticket system
	HttpMethod          string               `json:"http_method"` // method for ticket request
	Auth                AuthInfo             `json:"auth"`        //  auth information for ticket request
	SiteKeyAndEndpoints []SiteKeyAndEndpoint `json:"site_key_and_endpoints"`
	RequestParams       RequestParams        `json:"request_params"` // request_params request params of ticket system
	Status              string               `json:"status"`         // status of this ticket system, like "normal" "deleted"
}

type AuthInfo struct {
	Method string `json:"method"` // type of auth, like "basic/bear token"...
	Token  string `json:"token"`  // auth token for request
}

type SiteKeyAndEndpoint struct {
	SiteKey  string `json:"site_key"` // not need for requests
	Endpoint string `json:"endpoint"` // not need for requests
	IDC      string `json:"idc"`      // not need for requests
}

type RequestParams struct {
	ParamGroup []Param `json:"param_group" bson:"param_group"`
}

type Param struct {
	KeyName     string   `json:"key_name" bson:"key_name" vd:"keyName($)"`                              // key name stored in database
	Value       string   `json:"value" bson:"value"`                                                    // param value
	DisplayName string   `json:"display_name" bson:"display_name"`                                      // key display name
	ElementType string   `json:"element_type" bson:"element_type" vd:"in($, 'int', 'float', 'string')"` // key type, e.g. int, float, string
	Structure   string   `json:"structure" bson:"structure" vd:"in($, 'single', 'list')"`               // array or not array, e.g. single, list
	Required    bool     `json:"required" bson:"required"`                                              // whether this param is required
	Editable    bool     `json:"editable" bson:"editable"`                                              // whether this param is editable
	Options     []string `json:"options" bson:"options"`                                                // option value of this param
}

function CreateRequest()
{
    var Request = false;

    if (window.XMLHttpRequest)
    {
        //Gecko, Safari, Konqueror
        Request = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        //Internet explorer
        try
        {
             Request = new ActiveXObject("Microsoft.XMLHTTP");
        }    
        catch (CatchException)
        {
             Request = new ActiveXObject("Msxml2.XMLHTTP");
        }
    }
 
    if (!Request)
    {
        alert("Error AJAX");
    }
    
    return Request;
} 

function SendRequest(r_method, r_path, r_args, r_handler)
{
    var Request = CreateRequest();
    if (!Request)
    {
        return;
    }

    Request.onreadystatechange = function()
    {
        if (Request.readyState == 4)
        {
            r_handler(Request);
        }
    }
    
    if (r_method.toLowerCase() == "get" && r_args.length > 0)
    r_path += "?" + r_args;
    
    Request.open(r_method, r_path, true);
    
    if (r_method.toLowerCase() == "post")
    {
        Request.setRequestHeader("Content-Type","application/json; charset=utf-8");
        Request.send(r_args);
   } else  {
        Request.send(null);
    }
} 


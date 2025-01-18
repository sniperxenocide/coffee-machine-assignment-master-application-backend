const GET = "GET";
const POST = "POST";
const PUT = "PUT";
const DELETE = "DELETE";

const baseURL = location.origin;
const loginAPI = "/authenticate";
const paymentTermAPI = "/api/v1/payment_term/all";

function showLoader() {
    document.getElementById("progress_loader_modal").style.display="block";
}

function hideLoader() {
    document.getElementById("progress_loader_modal").style.display="none";
}

function callAPI(url, method, body,callback) {
    showLoader();
    try {
        let xmlHttp = new XMLHttpRequest();
        xmlHttp.open(method, url, true);
        xmlHttp.setRequestHeader("Content-Type","application/json");
        let token = sessionStorage.getItem("token");
        if(token!=null){
            xmlHttp.setRequestHeader("Authorization", "Bearer "+token);
        }
        xmlHttp.onload = function (e) {
            hideLoader();
            if (xmlHttp.readyState === 4) {
                if (xmlHttp.status === 200) {
                    callback(xmlHttp.responseText);
                } else {
                    console.error(xmlHttp.response);
                    alert("Error: "+xmlHttp.response);
                }
            }
        };
        xmlHttp.onerror = function (e) {
            console.error(e);
            hideLoader();
        };
        xmlHttp.send(JSON.stringify(body));
        console.log(url + " " + xmlHttp.status);
    }catch (e) {hideLoader()}
}

function createSelectOption(value,text) {
    let option = document.createElement("option");
    option.text = text;
    option.value = value;
    return option;
}

function fetchAndLoadSelectOptions(url,selectID,value,text) {
    let x = document.getElementById(selectID);
    x.innerHTML = "";
    x.add(createSelectOption("%","ALL"));
    if (url.toString().endsWith("%")) {   // Checking if "ALL" selected
        try{x.onchange();}catch(e){}
        return;
    }
    callAPI(url,GET,null,fetchAndLoadCallback);
    function fetchAndLoadCallback(response) {
        try {
            let jsonArray = JSON.parse(response);
            if (jsonArray == null) return;
            for ( let i in jsonArray) {
                x.add(createSelectOption(jsonArray[i][value],jsonArray[i][text]));
            }
        } catch (e) {
            console.log(e);
        }
        try{x.onchange();}catch(e){}
    }
}

function loadSelectOptionFromJsonArray(jsonArray,selectId,value,text,defaultValue,defaultText) {
    let select = document.getElementById(selectId);
    select.innerHTML = "";
    if(defaultValue != null)
        select.appendChild(createSelectOption(defaultValue,defaultText));

    try {
        if (jsonArray == null) return;
        for ( let i in jsonArray) {
            select.appendChild(createSelectOption(jsonArray[i][value],jsonArray[i][text]));
        }
    } catch (e) {
        console.log(e);
    }
}

function allowOnlyIntegerInput(event){
    let c = parseInt(event.which || event.key);
    console.log(c);
    event.returnValue = (c >= 48 && c <= 57) || c==8 || (c>=96 && c<=105)
}


function modalAction(){
    let modal = document.getElementById("modal");
    let span = document.getElementsByClassName("close")[0];
    modal.style.display = "block";
    span.onclick = function() {
        modal.style.display = "none";
    }
}

function table_el_to_array(table_el) {
    let myData = table_el.rows;
    let my_list = [];
    for (let i = 0; i < myData.length; i++) {
        let el = myData[i].children;
        let my_el = [];
        for (let j = 0; j < el.length; j++) {
            my_el.push(el[j].innerText);
        }
        my_list.push(my_el);
    }
    return my_list;
}

function table_to_array(table_id) {
    let table_el = document.getElementById(table_id);
    return table_el_to_array(table_el);
}

function exportTable(tableId) {
    let wb = XLSX.utils.book_new();
    let data = table_to_array(tableId);
    let ws = XLSX.utils.aoa_to_sheet(data);
    XLSX.utils.book_append_sheet(wb, ws, 'data');
    XLSX.writeFile(wb, tableId+".csv");

}

// let wbout = XLSX.write(wb, {bookType:'xlsx',  type: 'binary'});
// saveAs(new Blob([s2ab(wbout)],{type:"application/octet-stream"}), 'test.xlsx');
//
// function s2ab(s) {
//     let buf = new ArrayBuffer(s.length); //convert s to arrayBuffer
//     let view = new Uint8Array(buf);  //create uint8array as viewer
//     for (let i=0; i<s.length; i++) view[i] = s.charCodeAt(i) & 0xFF; //convert to octet
//     return buf;
// }

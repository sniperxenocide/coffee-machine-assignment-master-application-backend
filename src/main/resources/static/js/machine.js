function init() {
    setHeader();
    setFooter();
    menuButtonColorChange(0);
}

function getSupplierNameFromCode(element){
    callAPI(baseURL+"/api/supplier?code="+element.value.toString(),GET,null,callback)
    function callback(response){
        try{
            let json = JSON.parse(response);
            if(json['status'])
                document.getElementById('supplier_name').value = json['data']['supplierName'];
            else throw json['msg'];
        }catch (e){
            alert(e);
            console.log(e);
            element.value = '';
            document.getElementById('supplier_name').value = '';
        }
    }
}

function formParamUpdate(element) {
    let form = document.getElementById('machine-add-edit-form');
    let cnt;
    let maxCnt = 600;
    if(element.value=='') cnt=1;
    else {
        cnt = parseInt(element.value);
        if(cnt<=1) cnt = 1;
        if(cnt>maxCnt) cnt = maxCnt;
        element.value = cnt;
    }
    form.action = form.action.split('?')[0]+'?cnt='+cnt;
    console.log(form.action);
}


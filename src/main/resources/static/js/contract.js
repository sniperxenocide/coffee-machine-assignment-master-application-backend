let paymentTerms = [];
let firstTime = false;
let isAdd = false;

let params = {
    'machineNumber':'','changeReason':''
}

function init() {
    setHeader();
    setFooter();
    loadPaymentTermList();
}

function machineSearchBoxSet(val) {
    let el = document.getElementById('machine-search-box');
    el.value = val;
    el.onchange() ;
}

function onChangeMachineSearchBox(contractId,shopId,machineId,add,changeMachine){
    let url = location.origin+'/contract'+
        (add?'/add?shopId='+shopId:'/edit?id='+contractId)+'&machineId='+machineId+
        (changeMachine?'&changeMachine=true':'');
    console.log(url);
}

function onClickChangeMachine(contractId){
    if(confirm('Are you sure you want to change Machine for this Shop ???')){
        location.replace(location.origin+'/contract/edit?id='+contractId+'&changeMachine=true');
    }
}

function appendMachineNumberWithCreate(val) {
    params['machineNumber'] = val.toString().trim();
    updateFormAction();

}

function onChangeReason(val){
    params['changeReason'] = val;
    updateFormAction();
}

function updateFormAction(){
    let form = document.getElementById('contract-form');
    let newAction = form.action.includes('create')?'/contract/create?':'/contract/update?';
    for(let key in params)
        if(params[key].length>0) newAction+=key+'='+params[key]+'&';
    newAction = newAction.slice(0,-1);
    form.action = newAction;
    console.log(newAction);
}

function loadPaymentTermList() {
    callAPI(baseURL+paymentTermAPI,GET,null,callback);
    function callback(response){
        try {
            let json = JSON.parse(response);
            if(json['status']) paymentTerms = json['data'];
            console.log(paymentTerms);

            if(document.getElementById('page-header').
                    innerText.toUpperCase().includes('ADD')){
                isAdd = true;
            }
            firstTime = true;
            document.getElementById('paymentTerm').onchange(event);

        }catch (e){console.log(e);}
    }
}

function onSelectDeleteReason(val,contractId){
    let form = document.getElementById('contract-delete-form');
    if(val.length>0){
        form.action = '/contract/delete?id='+contractId+'&deleteReason='+val;
    }
}


function onPaymentTermChange(selectedVal){
    let idx = 0;
    for(let i in paymentTerms){
        if(paymentTerms[i]['id']==selectedVal) {
            idx = i;
            break;
        }
    }


    for(let p in paymentTerms[idx]){
        if(p=='id') continue;
        try {
            if(paymentTerms[idx][p]!=null){
                if(isAdd || !firstTime)
                    document.getElementById(p).value = paymentTerms[idx][p];
                document.getElementById(p).readOnly = true ;
                document.getElementById(p).style.backgroundColor = '#fcf4d9';
            }
            else {
                if(isAdd || !firstTime)
                    document.getElementById(p).value = '';
                document.getElementById(p).readOnly = false ;
                document.getElementById(p).style.backgroundColor = '#E5ECEC';
            }
        }catch (e) {}
    }
    firstTime = false;
}

function onChangeAllotmentPrice(price) {
    document.getElementById('downPayment').value = price;
    document.getElementById('downPayment').oninput(event);
}

function onChangeDownPayment(element) {
    if(element.readOnly) return;
    document.getElementById('perInstallmentAmount').value =
    document.getElementById('machineAllotmentPrice').value - element.value;
    document.getElementById('numberOfInstallment').value = 1;
}

function onChangeNumberOfInstallment(element) {
    if(element.value<1) element.value = 1;
    document.getElementById('perInstallmentAmount').value =
        (document.getElementById('machineAllotmentPrice').value -
        document.getElementById('downPayment').value)/element.value;
}

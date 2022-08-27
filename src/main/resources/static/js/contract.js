let paymentTerms = [];
let firstTime = false;
let isAdd = false;

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


function appendMachineNumberWithCreate(val) {
    let form = document.getElementById('contract-form');
    let param = '';
    if(val.toString().trim().length>=3){
        param = '?machineNumber='+val.toString().trim();
    }
    form.action = '/contract/create' + param;
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

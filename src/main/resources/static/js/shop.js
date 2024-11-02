function init() {
    setHeader();
    setFooter();
    menuButtonColorChange(1);
}

function getDistributorNameFromCode(element){
    callAPI(baseURL+"/api/distributor?code="+element.value.toString(),GET,null,callback)
    function callback(response){
        try{
            let json = JSON.parse(response);
            console.log(json);
            if(json['status'])
            {
                document.getElementById('distributorName').value = json['data']['customerName'];
                document.getElementById('division').value = json['data']['division'];
                document.getElementById('region').value = json['data']['region'];
                document.getElementById('territory').value = json['data']['territory'];
            }
            else throw json['msg'];
        }catch (e){
            alert(e);
            console.log(e);
            element.value = '';
            document.getElementById('distributorName').value = '';
            document.getElementById('division').value = '';
            document.getElementById('region').value = '';
            document.getElementById('territory').value = '';
        }
    }
}

function onShopCategoryChange(categoryText) {
    let ids = ['distributorOracleCode','distributorName','division',
                'region','territory']
    let str = categoryText.toString().toLowerCase().replace(' ','');
    if(str.includes('intercompany')){
        for(let i in ids){
            let e = document.getElementById(ids[i]);
            e.required = false;
            e.previousElementSibling.children[1].style.display = 'none';
        }
    }
    else {
        for(let i in ids){
            let e = document.getElementById(ids[i]);
            e.required = true;
            e.previousElementSibling.children[1].style.display = 'inline';
        }
    }

}
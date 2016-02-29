(function(){

    function examsService($http) {

        function Contract() {
            var that = this;

            this.removeContract = function() {
                return $http["delete"](root + '/contract/' + this.contractNumber);
            };

            this.save = function() {
                return $http.post(root + '/contract', that)
                    .then(function(response){
                        return response.data;
                    });
            };

            this.update = function() {
                return $http.put(root + '/contract/' + this.contractNumber, this)
                    .then(function(response){
                        that = response.data;
                        return that;
                    });
            };

            this.activate = function() {
                return $http.patch(root + '/contract/' + this.contractNumber, this)
                    .then(function(response){
                        return response.data;
                    });
            };

            this.addSerialDeviceToContract = function(serialList){
                return $http.put(root + '/contracts/' + this.contractNumber + '/device', serialList)
                    .then(function(response){
                        return response.data;
                    });
            };

            this.removeDeviceLinked = function(serial){
                return $http["delete"](root + '/contracts/' + this.contractNumber + '/device/' + serial);
            };
        }

        Contract.all = function() {
            return $http.get(root + '/contracts?page=1')
                .then(function(response){
                    return response.data;
                });
        };

        Contract.allContracts = function(page) {
            return $http.get(root + '/contracts?page=' + page )
                .then(function(response){
                    return response.data;
                });
        };

        Contract.allContractsByParam = function(parameter, page) {
            return $http.get(root + '/contracts?text=' + parameter + '&page=' + page)
                .then(function(response){
                    return response.data;
                });
        };

        Contract.listContractsByAdvancedSearch = function(advancedSearch, page){
            return $http.get(root + '/contracts?' + advancedSearch + '&page=' + page )
                .then(function(response){
                    return response.data;
                });
        };

        Contract.listByStatus = function(contractStatus, page){
            return $http.get(root + '/contracts?status=' + contractStatus + '&page=' + page )
                .then(function(response){
                    return response.data;
                });
        };

        Contract.readingDays = function(){
            return [1,5,10,15,20,25,30];
        };

        Contract.contractsTypes = function(){
            return ['CONTRATO DE LOCACAO SIMPLES'];
        };

        return Contract;
    }

    angular.module('mainModule')
        .factory('Exam', examService);
})();
/**
 * Created by andersonjso on 2/24/16.
 */

var app = angular.module('mainApp', ['ngTable', 'ngResource']);

app.controller('tableController', ['$scope', '$http', function($scope, $http, ngTableParams){
    $http.get("http://localhost:8080/exams/1")
        .then(function(response) {
            $scope.exams = response.data;
        });

   }]);

/*

 Clients.listByStatus = function(clientStatus, page){
 return $http.get(root + '/clients?status=' + clientStatus + '&page=' + page )
 .then(function(response){
 return response.data;
 });
 };
 <script>
 var app = angular.module('myApp', []);
 app.controller('myCtrl', function($scope, $http) {
 $http.get("welcome.htm")
 .then(function(response) {
 $scope.myWelcome = response.data;
 });
 });
 </script>
 */
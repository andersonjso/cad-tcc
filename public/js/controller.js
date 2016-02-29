/**
 * Created by andersonjso on 2/13/16.
 */

/** Controllers **/

var mainApp = angular.module('mainApp', []);

mainApp.controller('ListAttrCtrl', ['$scope', '$http', function($scope, $http){

    /*
    $http.get('json/example.json').success(function(data){
        $scope.attributes = data;
    });
*/

    $scope.attributes = [
        {'color': 'att1', 'value': 'value1'},
        {'color': 'att2', 'value': 'value2'},
        {'color': 'att3', 'value': 'value3'}
    ];

    $scope.attributes2 = [
        {'color': 'att1 2', 'value': 'value1'},
        {'color': 'att2 2', 'value': 'value2'},
        {'color': 'att3 2', 'value': 'value3'}
    ];


}]);


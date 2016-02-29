/**
 * Created by andersonjso on 2/24/16.
 */

var app = angular.module('mainApp', ['ngTable', 'ngResource', 'ngRoute']);

/** CONFIG **/
app.config(function($routeProvider){
    $routeProvider
        .when('/', {
            templateUrl: 'pages/exams.html',
            controller: 'examsController'
        })
        .when('/exam/:path', {
            templateUrl: 'pages/exam.html',
            controller: 'examController',
        })
        .otherwise({redirectTo: '/'})
})

app.controller('examsController', ['$scope', '$location', 'dataFactory',
    function($scope, $location, dataFactory){

        $scope.allExams;
        getExams();

        function getExams(){
            dataFactory.listExams()
                .success(function (response){
                    $scope.allExams = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                })
        }

        $scope.goToExam = function(exam) {
            $location.path('/exam/' + exam.path.substring(11, 25));
        };
}]);

app.controller('examController', ['$scope', '$routeParams', 'dataFactory',
    function ($scope, $routeParams, dataFactory) {

    var path = $routeParams.path;

    retrieveExamByPath();

    function retrieveExamByPath(){
        dataFactory.retrieveExamByPath(path)
            .success(function (response){
                $scope.exam = response;
            })
            .error(function (error){
                $scope.status = 'Unable to load data: ' + error.message;
            })
    }
}]);

app.factory('dataFactory', ['$http', function($http){
    var dataFactory = {};

    dataFactory.listExams = function(){
        return $http.get('exams')
    }

    dataFactory.retrieveBigNodulesFromExam = function(examPath){
        return $http.get('exam/' + examPath + '/bignodules')
    }

    dataFactory.retrieveSimilarNodules = function(examPath, noduleId){
        return $http.get('exam/' + examPath + '/nodule/' + noduleId + '/similar')
    }

    dataFactory.retrieveExamByPath = function(examPath){
        return $http.get('exam/' + examPath);
    }

    return dataFactory;
}])

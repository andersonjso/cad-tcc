/**
 * Created by andersonjso on 2/24/16.
 */

var app = angular.module('mainApp', ['ngTable', 'ngResource', 'ngRoute', 'ui.bootstrap', 'ngTouch', 'ngTable']);

/** CONFIG **/
app.config(function($routeProvider){
    $routeProvider
        .when('/', {
            templateUrl: 'pages/exams.html',
            controller: 'examsController',
        })
        .when('/exam/:path', {
            templateUrl: 'pages/exam.html',
            controller: 'examController',
        })
        .when('/nodules', {
            templateUrl: 'pages/nodules.html',
            controller: 'myNodulesController',
        })
        .otherwise({redirectTo: '/'})
})

app.controller('examsController', ['$scope', '$location', '$route', 'dataFactory', '$uibModal', 'NgTableParams',
    function($scope, $location, $route, dataFactory, $uibModal, NgTableParams){

        $scope.allExams;
        getExams();
        $scope.maxSize = 5;
        $scope.queryIsOn = false;

        $scope.sendPost = function() {
            var filesSelected = document.getElementById("inputFileToLoad").files;

            $scope.numberOfFiles = filesSelected.length;
            var preview = document.querySelector('#preview');
            var imagesToSave = [];

            function readFile(file){
                var reader = new FileReader();

                reader.addEventListener("load", function () {
                    var jsonObject = {"imageEncoded": this.result.substring(22)};

                    imagesToSave.push(jsonObject);
                }, false);

                reader.readAsDataURL(file);
            }

            if (filesSelected){
                [].forEach.call(filesSelected, readFile);
            }

            setTimeout(function(){
                $scope.similarNodulesOf3D = {};

                dataFactory.retrieveSimilarNodulesFrom3D(imagesToSave)
                    .success (function(response){
                        $scope.similarNodulesOf3D = response;
                        $scope.slicesNodule3D = imagesToSave;
                    })
                    .error(function (error){
                        $scope.status = 'Unable to load data: ' + error.message;
                    })

                dataFactory.retrieveTexture(imagesToSave)
                    .success (function(response){
                        $scope.textureAtt = response;
                    })
                    .error(function (error){
                        $scope.status = 'Unable to load data: ' + error.message;
                    })


            }, 100);
        }

        $scope.openSimilar = function(){
            var modal = $uibModal.open({
                templateUrl: 'pages/nodule3d-modal.html',
                controller: 'nodule3DModalController',
                size: 'lg',
                scope: $scope
            });
        }


        function getExams(){
            dataFactory.listExams(1)
                .success(function (response){
                    $scope.allExams = response;
                    $scope.totalItems = response.totalPages * 10;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                })
        }

        function demoController(NgTableParams, simpleList) {
            this.tableParams = new NgTableParams({
                // initial sort order
                sorting: { name: "asc" }
            }, {
                dataset: simpleList
            });
        }



        $scope.goToExam = function(exam) {
            $location.path('/exam/' + exam.path.substring(11, 25));
        };

        $scope.showQuantityNodulesLvl = function (exam, lvl){
            var bigNodules = exam.readingSession.bignodule;
            var quantity = 0;
            for (var i = 0; i<bigNodules.length; i++){
                if (bigNodules[i].malignancy == lvl){
                    quantity++;
                }
            }
            return quantity;
        };


        $scope.pageChanged= function() {
            if ($scope.queryIsOn){
                dataFactory.retrieveExamsByPath($scope.searchExam, $scope.currentPage)
                    .success(function (response){
                        $scope.allExams = response;
                        $scope.totalItems = response.totalPages * 10;
                    })
                    .error(function (error){
                        $scope.status = 'Unable to load data: ' + error.message;
                    });
            }else {
                dataFactory.listExams($scope.currentPage)
                    .success(function (response) {
                        $scope.allExams = response;
                        $scope.totalItems = response.totalPages * 10;
                    })
                    .error(function (error) {
                        $scope.status = 'Unable to load data: ' + error.message;
                    })
            }
        };

        $scope.search = function(){
            event.preventDefault();
            $scope.queryIsOn = true;
            dataFactory.retrieveExamsByPath($scope.searchExam, 1)
                .success(function (response){
                    $scope.allExams = response;
                    $scope.totalItems = response.totalPages * 10;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        $scope.reload = function(){
            $route.reload();
        }
}]);

app.controller('myNodulesController', ['$scope', '$location', '$route', 'dataFactory', '$uibModal',
    function($scope, $location, $route, dataFactory, $uibModal){

        $scope.myNodules;
        getNodules();
        $scope.maxSize = 5;

        function getNodules(){
            dataFactory.listNodules(1)
                .success(function (response){
                    $scope.myNodules = response;
                    //console.log(JSON.stringify($scope.myNodules));
                    $scope.totalItems = response.totalPages * 10;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                })
        }

        $scope.openDetailsMyNodule = function(nodule){
            $scope.myActualNodule = nodule;
            var modal = $uibModal.open({
                templateUrl: 'pages/my-nodule-modal.html',
                controller: 'myNodulesModalController',
                size: 'lg',
                scope: $scope
            });
        };

        //todo here

        $scope.pageChanged= function() {

        };
    }]);

app.controller('myNodulesModalController', ['$scope', '$uibModal', 'dataFactory',
    function ($scope, $uibModal, dataFactory){

        console.log($scope.myActualNodule.rois);
        var getImagesNodules = function(path, id) {
            $scope.similarNoduleImg = {};

            dataFactory.retrieveBigNoduleImage(path, id)
                .success(function(response){
                    $scope.similarNoduleImg[id] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        var getRoiSlicesNodule = function (path, id, roiNumber){
            $scope.selectedNoduleRoisImg = {};

            dataFactory.retrieveNoduleSlices(path, id, roiNumber)
                .success(function(response){
                    $scope.selectedNoduleRoisImg[id+roiNumber] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }


        function getRoiSlicesMyNodule (id, roiNumber){
            $scope.nodulesRoisImg = {};

            dataFactory.retrieveMyNodulesSlices(id, roiNumber)
                .success(function(response){
                    $scope.nodulesRoisImg[id+roiNumber] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }


        retrieveSimilarNodules();

        for (var k = 0; k < $scope.myActualNodule.rois.length; k++){
            getRoiSlicesMyNodule($scope.myActualNodule.noduleId, k);
        }


        function retrieveSimilarNodules(){
            dataFactory.retrieveMySimilarNodules($scope.myActualNodule.noduleId)
                .success(function (response){
                    $scope.similarNodules = response;

                    for (var i = 0; i < $scope.similarNodules.length; i++){
                        var id = $scope.similarNodules[i].idNodule;
                        var path = $scope.similarNodules[i].path.substring(11, 25);
                        getImagesNodules(path, id);

                    }
                })
                .error(function (error){

                    $scope.status = 'Unable to load data: ' + error.message;
                })
        }

        $scope.openDetails = function (similarNodule) {
            $scope.isSimilar = true;
            $scope.pathToShow = similarNodule.path.substring(11, 25);
            var path = $scope.pathToShow;
            var id = similarNodule.idNodule;
            $scope.similarNoduleSelected = {};

            //console.log(path + " and " + id);
            dataFactory.retrieveBigNodule(path, id)
                .success(function(response){
                    $scope.similarNoduleSelected = response;

                    for (var i=0; i<$scope.similarNoduleSelected.rois.length; i++){
                        getRoiSlicesNodule(path, id, i);
                      //  getRoiSlicesMyNodule(id, i);
                    }

                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        $scope.backToSimilar = function(){
            $scope.isSimilar = false;
        }

        $scope.min = 0;
        $scope.max = 200;
        $scope.brigthValue = 100;
        $scope.contValue = 100;
        $scope.brigthValueSimilar = 100;
        $scope.contValueSimilar = 100;


        $scope.brigthChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.contrastChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.deleteNodule = function(){
            dataFactory.deleteNodule($scope.myActualNodule.noduleId)
                .success(function(response){
                    alert('Deletado :(');
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });

        }
    }]);



app.controller('nodule3DModalController', ['$scope', '$uibModal', 'dataFactory',
    function ($scope, $uibModal, dataFactory) {

        var getImagesNodules = function(path, id) {
            $scope.similarNoduleImg = {};

            dataFactory.retrieveBigNoduleImage(path, id)
                .success(function(response){
                    $scope.similarNoduleImg[id] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        retrieveSimilarNodules();

        var getRoiSlicesNodule = function (path, id, roiNumber){
            $scope.selectedNoduleRoisImg = {};

            dataFactory.retrieveNoduleSlices(path, id, roiNumber)
                .success(function(response){
                    $scope.selectedNoduleRoisImg[id+roiNumber] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        function retrieveSimilarNodules(){
            $scope.similarNodules = $scope.similarNodulesOf3D;

            for (var i = 0; i < $scope.similarNodules.length; i++) {
                var id = $scope.similarNodules[i].idNodule;
                var path = $scope.similarNodules[i].path.substring(11, 25);
               getImagesNodules(path, id);
            }
        }

        $scope.openDetails = function (similarNodule) {
            $scope.isSimilar = true;
            $scope.pathToShow = similarNodule.path.substring(11, 25);
            var path = $scope.pathToShow;
            var id = similarNodule.idNodule;
            $scope.similarNoduleSelected = {};

            //console.log(path + " and " + id);
            dataFactory.retrieveBigNodule(path, id)
                .success(function(response){
                    $scope.similarNoduleSelected = response;

                    for (var i=0; i<$scope.similarNoduleSelected.rois.length; i++){
                        getRoiSlicesNodule(path, id, i);
                    }

                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        $scope.backToSimilar = function(){
            $scope.isSimilar = false;
        }

        $scope.min = 0;
        $scope.max = 200;
        $scope.brightValue3D = 100;
        $scope.contValue3D = 100;
        $scope.brigthValueSimilar = 100;
        $scope.contValueSimilar = 100;

        $scope.brightChange3D = function(bright, cont){
            for (var i = 0; i<$scope.similarNodules.length; i++){
                var scopeName = 'noduleSlice' +i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.contrastChange3D = function(bright, cont){
            for (var i = 0; i<$scope.similarNodules.length; i++){
                var scopeName = 'noduleSlice' +i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.brigthChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.contrastChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.saveNodule = function(){
            var noduleToSave = $scope.newNodule;

            noduleToSave.textureAttributes = $scope.textureAtt;

            var noduleDTO = {"bigNodule": noduleToSave, "encodedList": $scope.slicesNodule3D}
            //console.log(JSON.stringify(noduleDTO));

            dataFactory.createNodule(noduleDTO)
                .success(function(response){
                    alert("Criouu");
                    //console.log(response);
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
            /*
             dataFactory.retrieveBigNodule(path, id)
             .success(function(response){
             $scope.similarNoduleSelected = response;

             for (var i=0; i<$scope.similarNoduleSelected.rois.length; i++){
             getRoiSlicesNodule(path, id, i);
             }

             })
             .error(function (error){
             $scope.status = 'Unable to load data: ' + error.message;
             });
             */
        }

}]);



app.controller('examController', ['$scope', '$routeParams', 'dataFactory', '$uibModal', '$compile',
    function ($scope, $routeParams, dataFactory, $uibModal, $compile) {

        var path = $routeParams.path;
        $scope.imageCharged = false;
        $scope.statusButton = 'Zoom desativado';

        retrieveExamByPath();
        //retrieveImageExamByPath();

        var getImagesNodules = function(id) {
            $scope.noduleImg = {};

            dataFactory.retrieveBigNoduleImage(path, id)
                .success(function(response){
                    $scope.noduleImg[id] = response;
                   // var compiled = $compile($scope.noduleImg[id]);
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        };

        var getImagesExams = function(id){
            $scope.examImg = {};

            dataFactory.retrieveImageExamByPath(path, id)
                .success(function(response){
                    $scope.examImg[id] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        };

        var getRoiImageExams = function (id, roiNumber){
            $scope.roisImg = {};

            dataFactory.retrieveExamSlices(path, id, roiNumber)
                .success(function(response){
                    $scope.roisImg[id+roiNumber] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        var getRoiSlicesExams = function (id, roiNumber){
            $scope.nodulesRoisImg = {};

            dataFactory.retrieveNoduleSlices(path, id, roiNumber)
                .success(function(response){
                    $scope.nodulesRoisImg[id+roiNumber] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        function retrieveExamByPath(){
            dataFactory.retrieveExamByPath(path)
                .success(function (response){
                    $scope.exam = response;
                    $scope.bigNodules = $scope.exam.readingSession.bignodule;

                    for (var i = 0; i < $scope.bigNodules.length; i++){
                        var id = $scope.bigNodules[i].noduleId;
                        var rois = $scope.bigNodules[i].rois;

                        getImagesNodules(id);
                        getImagesExams(id);

                        for (var j = 0; j < rois.length; j++){
                            getRoiImageExams(id, j);
                            getRoiSlicesExams(id, j);
                        }
                    }
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                })
        };

        $scope.openNoduleDetails = function(actualNodule){
            $scope.actualNodule = actualNodule;
            $scope.pathExam = path;
            var modal = $uibModal.open({
                templateUrl: 'pages/nodule-modal.html',
                controller: 'noduleModalController',
                size: 'lg',
                scope: $scope
            });
        };

        $scope.allowZoom = function(){
          //  $scope.imageCharged ? $scope.imageCharged = false : $scope.imageCharged = true;
        }

        $scope.toggleZoom = function(){
            $scope.imageCharged ? $scope.imageCharged = false : $scope.imageCharged = true;

            if ($scope.imageCharged){
                $scope.statusButton = 'Zoom ativado'
            }
            else{
                $scope.statusButton = 'Zoom desativado'
            }
        }

        $scope.min = 0;
        $scope.max = 200;
        $scope.brigthValue = 3000;
        $scope.contValue = 400;

        $scope.brigthChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.contrastChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };
    }]);

app.controller('noduleModalController', ['$scope', '$uibModal', 'dataFactory',
    function ($scope, $uibModal, dataFactory) {

        var malign = 0;
        var benign = 0;
        retrieveSimilarNodules();

        var getImagesNodules = function(path, id) {
            $scope.similarNoduleImg = {};

            dataFactory.retrieveBigNoduleImage(path, id)
                .success(function(response){
                    $scope.similarNoduleImg[id] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        var getRoiSlicesNodule = function (path, id, roiNumber){
            $scope.selectedNoduleRoisImg = {};

            dataFactory.retrieveNoduleSlices(path, id, roiNumber)
                .success(function(response){
                    $scope.selectedNoduleRoisImg[id+roiNumber] = response;
                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        function retrieveSimilarNodules(){
            dataFactory.retrieveSimilarNodules($scope.pathExam, $scope.actualNodule.noduleId)
                .success(function (response){
                    $scope.similarNodules = response;

                    for (var i = 0; i < $scope.similarNodules.length; i++){
                        var id = $scope.similarNodules[i].idNodule;
                        var path = $scope.similarNodules[i].path.substring(11, 25);
                        getImagesNodules(path, id);

                        if ($scope.similarNodules[i].malignancy == 5){
                            malign++;
                        }
                        else{
                            benign++;
                        }
                    }

                    $scope.numberMalign = malign;
                    $scope.numberBenign = benign;

                })
                .error(function (error){

                    $scope.status = 'Unable to load data: ' + error.message;
                })
        }

        $scope.openDetails = function (similarNodule) {
            $scope.isSimilar = true;
            $scope.pathToShow = similarNodule.path.substring(11, 25);
            var path = $scope.pathToShow;
            var id = similarNodule.idNodule;
            $scope.similarNoduleSelected = {};

            //console.log(path + " and " + id);
            dataFactory.retrieveBigNodule(path, id)
                .success(function(response){
                    $scope.similarNoduleSelected = response;

                    for (var i=0; i<$scope.similarNoduleSelected.rois.length; i++){
                        getRoiSlicesNodule(path, id, i);
                    }

                })
                .error(function (error){
                    $scope.status = 'Unable to load data: ' + error.message;
                });
        }

        $scope.backToSimilar = function(){
            $scope.isSimilar = false;
        }

        $scope.min = 0;
        $scope.max = 200;
        $scope.brigthValue = 100;
        $scope.contValue = 100;
        $scope.brigthValueSimilar = 100;
        $scope.contValueSimilar = 100;

        $scope.brigthChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };

        $scope.contrastChange = function(bigNodule, bright, cont){
            for (var i = 0; i<bigNodule.rois.length; i++){
                var scopeName = 'nodule' + bigNodule.noduleId+i;

                //console.log($scope[scopeName]);

                $scope[scopeName] = {
                    'webkit-filter' : 'brightness(' + bright + '%) contrast(' + cont + '%)'
                }
            }
        };
}]);


app.factory('dataFactory', ['$http', function($http){
    var dataFactory = {};

    dataFactory.listExams = function(page){
        return $http.get('exams/' + page)
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

    dataFactory.retrieveImageExamByPath = function(examPath, noduleReference){
        return $http.get('exam/image/' + examPath + "/nodule/" + noduleReference);
    }

    dataFactory.retrieveBigNoduleImage = function(examPath, noduleId){
        return $http.get('exam/' + examPath + '/bignodules/' + noduleId);
    }

    dataFactory.retrieveSimilarNodules = function(examPath, noduleId){
        return $http.get('exam/' + examPath + "/nodule/" + noduleId + '/similar')
    }

    dataFactory.retrieveExamsByPath = function(examPath, page){
        return $http.get('exams/' + examPath + "/" + page)
    }

    dataFactory.retrieveExamSlices = function(examPath, noduleId, roiNumber){
        return $http.get('exam/' + examPath + "/nodule/" + noduleId + "/slices/" + roiNumber);
    }

    dataFactory.retrieveNoduleSlices = function(examPath, noduleId, roiNumber){
        return $http.get('exam/' + examPath + "/nodule-images/" + noduleId + "/slices/" + roiNumber);
    }

    dataFactory.retrieveBigNodule = function(examPath, noduleId){
        return $http.get('exam/' + examPath + "/big-nodule/" + noduleId);
    }

    dataFactory.retrieveSimilarNodulesFrom3D = function(data){
        return $http.post('nodule/similar', data);
    }

    dataFactory.createNodule = function(data){
        return $http.post('nodule', data);
    }

    dataFactory.retrieveTexture = function(data){
        return $http.post('nodule/texture', data);
    }

    dataFactory.listNodules = function(page){
        return $http.get('nodules/' + page);
    }

    dataFactory.retrieveMySimilarNodules = function(noduleId){
        return $http.get('nodule/' + noduleId + '/similar');
    }

    dataFactory.retrieveMyNodulesSlices = function (noduleId, roiNumber){
        return $http.get('nodule/' + noduleId + '/slices/' + roiNumber);
    }

    dataFactory.deleteNodule = function(noduleId){
        return $http["delete"]('nodule/' + noduleId);
    }

    return dataFactory;
}])

app.filter('slice', function() {
    return function(arr, start, end) {
        return (arr || []).slice(start, end);
    };
});

app.directive('ngElevateZoom', function(){
    return{
        restrict: 'A',
        link: function(scope, element, attrs) {
            scope.$watch('imageCharged', function(newVal, oldVal){
                if (newVal){
                    element.attr('data-zoom-image',attrs.zoomImage);
                    $(element).elevateZoom({
                        zoomWindowFadeIn: 300,
                        zoomWindowFadeOut: 300,
                        lensFadeIn: 300,
                        lensFadeOut: 300,
                        scrollZoom : true,
                        zoomWindowWidth:200,
                        zoomWindowHeight:200
                    });
                }
                else{
                    $.removeData($(element), 'elevateZoom');//remove zoom instance from image

                    $('.zoomContainer').remove();// remove zoom container from DOM
                }
            });
        }
    };
});

app.directive('ngChangeFilter', function(){
    return{
        restrict: 'A',
        link: function(scope, element, attrs) {
            var showBright = function(value){
                document.getElementById("minhaImg").style["-webkit-filter"] = "brightness(" + value + "%)";
            }
            alert(attrs.id);
        }
    };
});
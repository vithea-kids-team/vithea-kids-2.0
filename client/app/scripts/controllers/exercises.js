'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:ExercisesCtrl
 * @description
 * # ExercisesCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
    .controller('ExercisesCtrl', function ($scope, $http, $log, alertService, $location, userService, $timeout) {
      

        $scope.loading = true;
        $scope.newexercise = {};
        $scope.exercises = [];
        $scope.resources = [];

        $scope.exerciseType = "text";

        /* GET - Exercises list */
        $http.get('app/listexercises').success(function (data) {
            
            $scope.exercises = data;
        })
        .error(function () {
            $scope.error = "An error has ocurred while loading exercises.";
            $scope.loading = false;
        });


        /******** TOPIC */
        $scope.topics = [{
            topicId: 0,
            topicDescription: "Selecionar..."
        }];

        /* GET - Topics list */
        $http.get('app/listtopics').success(function (data) {
            for (var i=0; i<data.length; i++){
                $scope.topics.push(data[i]);
            }
        })
        .then(function () {
            $scope.selected_topicnew = $scope.topics[0];            
            $scope.newexercise.topic = $scope.selected_topicnew.topicId;
        },
        /* error */
            function () {
            $scope.error = "An error has ocurred while loading topics.";
            $scope.loading = false;
        });

        /* Select topic dropdown list handler for creation */
        $scope.selectnewtopic = function (item) {
        	$scope.selected_topicnew = item;
			$scope.newexercise.topic = $scope.selected_topicnew.topicId;
        }

        /******** LEVEL */
        $scope.levels = [{
            levelId: 0,
            levelDescription: "Selecionar..."
        }];

        /* GET - Levels list */
        $http.get('app/listlevels').success(function (data) {
            for (var i=0; i<data.length; i++){
                $scope.levels.push(data[i]);
            }
        })
        .then(function () {
            $scope.selected_levelnew = $scope.levels[0];            
            $scope.newexercise.level = $scope.selected_levelnew.levelId;
        },
        /* error */
            function () {
            $scope.error = "An error has ocurred while loading levels.";
            $scope.loading = false;
        });

        /* Select level dropdown list handler for creation */
        $scope.selectnewlevel = function (item) {
        	$scope.selected_levelnew = item;
			$scope.newexercise.level = $scope.selected_levelnew.levelId;
        }

        /******** DISTRACTORS */
        
        $scope.newexercise.distractors = [];
        $scope.newexercise.distractorsImg = [];
        
        $scope.removeDistractor = function(i){
            $scope.newexercise.distractors.remove(i);
        }

        $scope.addDistractor = function(){
            $scope.newexercise.distractors.push($scope.newDistractor);
            $scope.newDistractor = "";
        }
            
        /* aux function to remove items from array */
        Array.prototype.remove = function(from, to) {
            var rest = this.slice((to || from) + 1 || this.length);
            this.length = from < 0 ? this.length + from : from;
            return this.push.apply(this, rest);
        };

        /******** RESOURCES */
        $http.get('app/listresources').success(function (data) {
            for (var i=0; i<data.length; i++){
                $scope.resources.push(data[i]);
            }
            $timeout(function() { 
                $("#stimulus").imagepicker({                    
                    changed : function (imageSelected) {
                        $scope.newexercise.stimulus =  $("#stimulus").val();
                    }
                });
                $("#answerImg").imagepicker({
                    initialized : function () {
                        $scope.newexercise.answerImg = $scope.resources[0].resourceId;                        
                    },
                    chnaged : function (imageSelected) {                        
                        $scope.newexercise.answerImg = $("#answerImg").val();
                    }
                }); 
                $("#distractorsImg").imagepicker({                    
                    changed : function (imageSelected) {                        
                        $scope.newexercise.distractorsImg = $("#distractorsImg").val();                        
                    },
                    limit: 3
                });                
            }, 0); // wait...
            
        })
        .error(
            function () {
            $scope.error = "An error has ocurred while loading resources.";
            $scope.loading = false;
        });

        /******** REGISTER Exercise */
        $scope.registerexercise = function () {

			$scope.loading = true;

			$http.post('app/registerexercise', this.newexercise).success(function (data) {		

				$scope.exercises.push(data);
				$scope.loading = false;
				$scope.addMode = false;
			})
				.error(function (data, status) {
					if (status === 400) {
						angular.forEach(data, function (value, key) {
                            /* TODO */
							if (key === 'password' || key === 'username' || key === 'passwordcheck' || key === 'firstname' || key === 'lastname' || key === 'gender' || key === 'birthdate') {
								alertService.add('danger', key + ' : ' + value);
							} else {
								alertService.add('danger', value.message);
							}
						});
					}
					if (status === 500) {
						alertService.add('danger', 'Internal server error!');
					}
					$scope.loading = false;
				});
		};


        //toggle add exercise
		$scope.toggleAdd = function () {
			$scope.addMode = !$scope.addMode;
		};
		

  });
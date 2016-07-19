'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:ChildrenCtrl
 * @description
 * # ChildrenCtrl
 * Controller of the clientApp
 */


angular.module('clientApp')
  .controller('ChildrenCtrl', function ($scope, $http, $log, alertService, $location, userService) {
	  $scope.loading = true;
	  
	  $scope.genders = [{
		  id: "Select",
		  name: "Selecionar..."
	  	}, {
	        id: "Male",
	        name: "Masculino"        
	    }, {
	        id: "Female",
	        name: "Feminino"        
	    }, {
	        id: "Other",
	        name: "Outro"        
	    }];
	  
		/* Select gender dropdown list handler for creation */
		$scope.selectnewgender = function (item) {
 
        $scope.selected_gendernew = item;
				$scope.newchild.gender = $scope.selected_gendernew.id;        
    }
		
	  $scope.selected_gendernew = $scope.genders[0];
		$scope.newchild = {};
		$scope.newchild.gender = $scope.selected_gendernew.id;

		/* Select gender dropdown list handler for edition */
		$scope.editgender = function (item) { 
        this.child.selected_gender = item;
				this.child.gender = this.child.selected_gender.id;
    }

	  /* GET - Children list */	  
	  $http.get('app/listchildren').success(function(data) {
		 angular.forEach(data, function(value, key) {
			 value.birthDate = sqlToJsDate(value.birthDate);

			 if (value.gender != null) {					
					var gender = $.grep($scope.genders, function(e){ return e.id === value.gender; });					
					value.gender = gender[0].name;
			 }
		 });
	     $scope.children = data;
	   })
	   .error(function () {
		   $scope.error = "An error has ocurred while loading children.";
		   $scope.loading = false;
	   });
	  
	  //toggle child edition
	  $scope.toggleEdit = function () {
		  this.child.editMode = !this.child.editMode;

			if(this.child.editMode) {
				if (this.child.gender != null) {
					var genderToCompare = this.child.gender;
					var gender = $.grep($scope.genders, function(e){ return e.name === genderToCompare; })
					this.child.selected_gender = gender[0];
				}
				else 
					this.child.selected_gender = $scope.genders[0];
			}
	  };
	  
	  //toggle add child
	  $scope.toggleAdd = function () {
		  $scope.addMode = !$scope.addMode;
	  };
	  
	  /* REGISTER child */
      $scope.registerchild = function() {
    	  
    	  $scope.loading = true;
    	  

          $http.post('app/registerchild', this.newchild).success(function (data) {

						
						data.birthDate = sqlToJsDate(data.birthDate);

						$scope.children.push(data);        	  
        	  $scope.loading = false;
        	  $scope.addMode = false;        	  
          })
          .error(function(data, status) {        	
						if(status === 400) {
							angular.forEach(data, function(value, key) {
								if(key === 'password' || key === 'username' || key === 'passwordcheck' || key === 'firstname' || key === 'lastname' || key === 'gender' || key === 'birthdate') {
									alertService.add('danger', key + ' : ' + value);
								} else {
									alertService.add('danger', value.message);
								}
							});
						}
						if(status === 500) {
							alertService.add('danger', 'Internal server error!');
						}
						$scope.loading = false;
					});
        };
     
     /* EDIT child */
     $scope.editchild = function() {

    	 $scope.loading = true;
    	 
    	 var childToEdit = this.child;

         $http.post('app/editchild/' + childToEdit.childId, childToEdit).success(function (data) { 
				
        	 childToEdit.editMode = false;
        	 $scope.loading = false;
         })
         .error(function(data, status) {
           if(status === 400) {
             angular.forEach(data, function(value, key) {
               if(key === 'editusername' || key === 'editfirstname' || key === 'editlastname' /*|| key === 'gender'*/ || key === 'editbirthdate') {
                 alertService.add('danger', key + ' : ' + value);
               } else {
                 alertService.add('danger', value.message);
               }
             });
           }
           if(status === 500) {
             alertService.add('danger', 'Internal server error!');
           }
         });

				 var genderToCompare = this.child.gender;
				if (genderToCompare != null) {					
						var gender = $.grep($scope.genders, function(e){ return e.id === genderToCompare});
				 		this.child.gender = gender[0].name;
				}           
       };
       
       /* DELETE child */
       $scope.deletechild = function () {
    	   
    	   $scope.loading = true;
    	   
    	   var childToDelete = this.child.childId;
    	   
    	   $http.post('app/deletechild/'+ childToDelete).success(function (data) {
    		   $.each($scope.children, function (i) {
    			   if ($scope.children[i].childId === childToDelete) {
    				   $scope.children.splice(i, 1);
    				   return false;
    			   }
    		   });
    		   $scope.loading = false;
    	   })
           .error(function(data, status) {
             if(status === 400) {
               angular.forEach(data, function(value, key) {
                 alertService.add('danger', value.message);                 
               });
             }
             if(status === 500) {
               alertService.add('danger', 'Internal server error!');
             }
           });
       };


			 /* SQL Date to JS */
			 function sqlToJsDate(sqlDate){
					//sqlDate in SQL DATETIME format ("yyyy-mm-dd")
					var sqlDateArr1 = sqlDate.split("-");
					//format of sqlDateArr1[] = ['yyyy','mm','dd']
					var sYear = sqlDateArr1[0];
					var sMonth = (Number(sqlDateArr1[1]) - 1).toString();
					var sqlDateArr2 = sqlDateArr1[2].split(" ");
					//format of sqlDateArr2[] = ['dd']
					var sDay = sqlDateArr2[0];
										
					return new Date(sYear,sMonth,sDay);
			}       
  });
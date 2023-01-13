pipeline
{
    agent any
        tools{
        maven 'maven'
        allure 'allure'
        jdk 'java'
        }


    stages
    {


        stage('Git') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {

                                git branch: "main",
                                    url: "https://github.com/yann-richard/PlaywrightNatif-main.git"


                }
            }
        }



                stage('Regression Automation Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                             bat	"echo TEST"
                             bat "mvn clean test"



                }
            }
        }

                stage("Allure Report generation"){
                    steps{
            allure([
                includeProperties : true,
                jdk : 'java',
                properties : [[key: 'release version', value: '4.0.2']],
                reportBuildPolicy : 'ALWAYS',
                results : [[path: 'allure-results']]
            ])
        }}

//         stage('Publish Extent Report'){
//             steps{
//                      publishHTML([allowMissing: false,
//                                   alwaysLinkToLastBuild: false,
//                                   keepAll: true,
//                                   reportDir: 'build',
//                                   reportFiles: 'TestExecutionReport.html',
//                                   reportName: 'HTML Extent Report',
//                                   reportTitles: ''])
//             }
//         }




    }
}
### Commands to build and run docker image locally
* docker build -t kube-service .
* docker run -p 8080:8080 kube-service

### To list all images
* docker images

### To find current running docker process
* docker ps -a

### To stop a container
* docker stop

### To remove an image
* docker rmi 

### To go inside the container
* docker exec -it /bin/sh

### To push docker image to docker hub after building the image
* docker login -u=azaveri7
* docker tag kube-service:latest azaveri7/docker-labs:kube-demo1
* docker push azaveri7/docker-labs:kube-demo1

### Steps to run docker image in minikube:
* minikube start
* minikube status
* minikube dashboard

### Install ingress controller
* minikube addons enable ingress #not required on katacoda
* kubectl get pods -n kube-system

# If you are using a katacoda or similar playground you can start here as you get a pre baked k8s master and node.

### Creating a Deployment using an existing Docker image
* kubectl run kube-demo --image=azaveri7/docker-labs:kube-demo1 --port=8080

The above command will use the tag kube-demo1 from the repository docker-labs at
deployment.

### Creating a service for the Deployment created in previous step
* kubectl expose pod kube-demo --target-port=8080 --type=NodePort

##For Minikube: 
* minikube service kube-demo --url will output the url to query
for eg:
```http://172.17.0.41:32492```

##For Katacoda (we need to work harder)
* kubectl get service -o wide (note down the nodeport like 31649 below) 
```
NAME         TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE    SELECTOR

kube-demo    NodePort    10.100.56.30   <none>        8080:**31649**/TCP   46s    run=kube-demo
kubernetes   ClusterIP   10.96.0.1      <none>        443/TCP          3m6s   <none>
```

* kubectl  get nodes -o wide #grab the worker node ip address below eg 172.17.0.93 as pods are intelligently placed only on worker/slave  nodes (with ROLES as not maseter)

```
NAME           STATUS   ROLES    AGE     VERSION   INTERNAL-IP   EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION       CONTAINER-RUNTIME

controlplane   Ready    master   5m35s   v1.18.0   172.17.0.40   <none>        Ubuntu 18.04.4 LTS   4.15.0-101-generic   docker://19.3.6
  
node01         Ready    <none>   4m54s   v1.18.0   **172.17.0.41**   <none>        Ubuntu 18.04.4 LTS   4.15.0-101-generic   docker://19.3.6
```

* With this you can create your application url as below with ```http://<node01_ip>:<nodeport_of_k8s-demo_pod>/employees ```

### Testing minikube deployment
* curl http://172.17.0.41:32492/employees/
```[{"id":1,"firstName":"Pradeep","lastName":"Singh","emailId":"Pradeep.Singh2@gmail.com"},{"id":2,"firstName":"Anand","lastName":"Zaveri","emailId":"Anand.Zaveri@gmail.com"},{"id":3,"firstName":"Akhilesh","lastName":"Juyal","emailId":"Akhilesh.Juyal@gmail.com"},{"id":4,"firstName":"Vaibhav","lastName":"Jha","emailId":"Vaibhav.Jha@outlook.com"},{"id":5,"firstName":"Himanshu","lastName":"Sharma","emailId":"Himanshu.Sharma@yahoo.com"},{"id":6,"firstName":"Jignesh","lastName":"Mehta","emailId":"Jignesh.Mehta@outlook.com"}]```

* curl http://172.17..0.41:32492/employees/2
```{"id":2,"firstName":"Anand","lastName":"Zaveri","emailId":"Anand.Zaveri@gmail.com"}```

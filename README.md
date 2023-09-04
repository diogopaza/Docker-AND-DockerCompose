# Docker-AND-DockerCompose
<p>O arquivo exemplo-docker-compose-postgresql.yml carrega imagem do postgres como base de exemplo.</p>
<h2>Importante - ARQUIVO DE EXEMPLO  exemplo-docker-compose-postgrtesql.yml</h2>
<h4>Volumes</h4>
<strong>/mnt/volume-docker:/var/lib/postgresql/data</strong> === no exemplo a pasta da máquina local 
<em>/mnt/volume-docker</em> tem os mesmos dados da pasta <em>/var/lib/postgresql/data</em> do container.<br/>   
<strong>pg-config:/etc/postgresql</strong> === <br/>



<h2>DOCKER</h2>

inicializar o serviço no Ubuntu == <strong>service docker start</strong>.

<strong>docker images</strong> == lista as imagens disponiveis.
<strong>docker start idContainer</strong> == inicia o container que está parado.<br/>
<strong>docker stop idContainer</strong> == para o container selecionado.<br/>
<strong>docker start idContainer</strong> == inicia o container que está parado.<br/>
<strong>docker ps</strong> == lista os containers em atividade.<br/>
<strong>docker inspect idImage</strong> == lista os detalhes da imagem.<br/>
<strong>docker history idImage</strong> == visualiza as camadas da imagem.<br/>
<strong>docker ps -a </strong> == lista todos os containers, incluindo os que estiverem parados.<br/>
<strong>docker run -d idContainer </strong> == flag -d o conainer é executado em segundo plano no terminal.<br/>
<strong>docker run -P idContainer</strong> == mapeia automaticamente as portas do container, com o -P maiúsculo não é possível especificar a porta.<br/>
<strong>docker run -p  portaDaMinhaMaquina:portaContainer idContainer</strong> == a flag -p (minúsculo) mapeia de forma específica as portas do container.<br/>
<strong>docker port idContainer</strong> == lista as portas do container.<br/>
<strong>docker rm idContainer --force</strong> == para e exclui um container docker.<br/>
<strong>docker system prune -a</strong> == limpa containeres nao utilizados.<br/>
<strong>docker volumes prune</strong> == remove volumes nao utilizados.<br/>
<strong>docker rmi <image_id></strong> == serve para excluir uma imagem do disco local.<br/>
<strong>docker rmi <image_id></strong> == define um nome para o Container.<br/>

exemplo de comando para inicializar uma imagem docker Mysql, importante observar o uso do atributo -p indicando a que a porta 3306 do
docker também será a porta 3306 do Linux que esta rodando no WSL do Windows.

 docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_USER=diogo -e MYSQL_PASSWORD=123 mysql:5.7 == este comando cria um novo container e aporta 3306 do host foi redirecionada para a porta 3306 do container, então quando é acessado http://localhost:3306 é acessado o container na porta 3306. Essa é uma regra de iptables.
 <p>Exemplo de container docker com postgres ===  <strong>docker run -v postgres-dados:/var/lib/postgresql/data -p 5432:5432 -e POSTGRES_PASSWORD=123 postgres</strong>. Pode-se adicionar a opção -d para rodar em segundo <em>background</em></p>
 
 iptables -t nat -L -n é possível ver o redirecionamento.

 <h3>Fazendo build a partir de um Dockerfile</h3>

 <strong>docker build -t nomeDaImagem:1.0 .</strong> == gera uma imagem a partir de um arquivo DockerFile, a flag -t é para definir o nome da imagem, e importante notar o 
 . (ponto) ao final do comando, este define onde deve ser executado no caso o . (ponto) é o diretório atual. <br/>
 

 <h3>Enviar a imagem para o Docker Hub</h3>
 <p>Para enviar a imagem para o Docker Hub é necessário seguir um a padrão de nomenclatura. A imagem deve iniciar com o nome do usuário usado no Docker Hub. Geralmente as imagens não são criadas seguindo esse padrão, dessa forma existe o comando <strong>docker tag</strong> para renomear a imagem gerada à partir do Dockerfile. Exemplo abaixo:</p>

 <strong>docker login -u nomeUsuarioNoDockerHub</strong> == faz login no Docker Hub.<br/>
 <strong>docker tag nomeDaImagem usuarioDockerHub/nomeDaImagem</strong> == .<br/>
 <strong>docker push nomdeDaImage</strong> == envia a imagem para o repositorio do Docker Hub.<br/>

 <h3>Persistindo dados em um container Docker</h3>

  <strong>docker run -it -v caminhaMaquinaLocal:/caminhoDentroDoContainer --- exemplo executado em teste === docker run -it -v D:\volume-docker:/app ubuntu bash </strong> == usando bind mounts, onde uma pasta da maquina local é compartilhada com o container Docker. Ou o container pare de funcionar, ou algo do tipo os dados continuam persistidos na máquina local.<br/>
  <strong>docker run -it --mount type=bind,source=D:\volume-docker2,target=/app2 ubuntu bash</strong> == exemplo para fazer um bind de uma pasta do host com o container Docker.<br/>
 
 <h3>Volumes docker</h3>
 <p>O próprio Docker gerencia os volumes, deixando muito mais seguro o processo de persistência dos dados.</p>
 
 <strong>docker volume create NOMEVOLUME</strong> === cria um novo volume docker.<br/>
 <strong>docker volume ls</strong> === lista os volumes docker disponíveis.<br/>
 <strong>docker volume inspect nomeDoVolume</strong> === exibe os detalhes do volume selecionado.<br/>
 <strong>docker run -it -v nomdeDoVolume:/pastaNoContainer nomeContainer bash</strong> === exemplo de uso de volume em um container docker.<br/> 
 
 <p>
 <strong>docker exec -it meu_container bash </strong><br>
 Esse comando irá executar um o bash que é nosso console no linux.
 A flag -i permite mapear a entrada do teclado para o bashs e -t reserva o terminal.

 <h3>Redes no Docker</h3>
<p>Com o comando <strong>docker inspect idContainer</strong> no final é possível ver a rede do docker, o nome da rede e o endereço ip do container.</strong></p>
<p>O Docker possue a rede <strong>host</strong>, ao usar essa rede é retirado o isolamento entre o Container e a máquina local, dessa forma sendo acessado os recursos do Docker diretamente via localhost, e também possue a rede <strong>none</strong> na qual o container fica sem uma interface de rede.</p>
  <strong>docker network ls</strong> === lista as redes Docker.<br/>
  <strong>docker network create --driver bridge nomeDaRede</strong> === comando para criar uma rede no Docker.<br/>

<h3>Comunicação através de redes Docker</h3>
<p>Abaixo o primeiro comando tem um servidor de um banco de dados mongo, este ficará responsável por disponibilizar os dados para uma aplicação, representada pelo segundo comando. A falg --name é de fundamental importância pois define o nome do host do banco de dados.</p>
<strong>docker run -d --network nomeDaRede --name meu-mongo mongo:4.4.6</strong> === cria o container com o banco de dados mongo.<br/>
<strong>docker run -d --network nomeDaRede --name nomeDoContainer -p 3000:3000</strong> === máquina com a aplicação backend que irá fazer uso do banco de dados mongo do container definido pelo nome de meu-mongo.<br/>


 
 <h2>DOCKER-COMPOSE</h2>
 <p>Ferramenta de coordenação de containers. Nos auxilia a compor diversos containers através de um único arquivo</p>
 <p>Configurado atraves de um arquivo <strong>YAML</strong> tem a capacidade de rodar varios containers, sem a necessidade de subir cada container manualmente.</p>
  <p>YAML (uma sigla que significa YAML Ain’t Markup Language) não é propriamente uma linguagem de programação mas sim uma sintaxe para a criação de ficheiros de configuração. A estrutura do YAML é determinada pela sua indentação, sendo a indentação definida pelo numero de espaços no inicio da cada linha e podem ser zero, dois ou múltiplos de dois. A estrutura de cada bloco é constituída por níveis, sendo que o nível anterior é considerado parent e o inferior children. O caracter - indica o inicio de uma listagem de variaveis ou atributos que se encontram ao mesmo nível, e que deve ser considerado na contagem de espaços na indentação. Em YAML os comentários são indicados pelo caracter #. Se o comentário estiver entre dois blocos de código deve ser escrito sem espaços à esquerda, se for escrito após uma linha de código deve sempre ser precedido de um espaço ou de uma ou mais tabulações para que os comentários fiquem alinhados.</p>

<strong>docker-compose up</strong> === subindo os containers que estiverem no arquivo .yml da pasta onde o comando foi executado.  
<strong>docker-compose -f <nomdeDoArquivo.yml> up</strong> === subindo os containers com um único comando.
 
 </p>
 <h2>Erro ao inicializar Docker no WSL</h2>
 <p>failed to start daemon: Error initializing network controller: error obtaining controller instance: unable to add return rule in DOCKER-ISOLATION-STAGE-1 chain:  (iptables failed: iptables --wait -A DOCKER-ISOLATION-STAGE-1 -j RETURN: iptables v1.8.7 (nf_tables):  RULE_APPEND failed (No such file or directory): rule in chain DOCKER-ISOLATION-STAGE-1
 (exit status 4))</p>
 <strong>Solução</strong>
 <p>sudo update-alternatives --set iptables /usr/sbin/iptables-legacy<br>
sudo update-alternatives --set ip6tables /usr/sbin/ip6tables-legacy
</p>

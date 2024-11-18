# 3D Pov-Ray Suite

Este repositório contém o código-fonte e a documentação do projeto **3D Pov-Ray Suite**, desenvolvido como parte da disciplina de Engenharia de Software da Universidade Federal de Alagoas (UFAL). O objetivo do projeto é criar um aplicativo desktop para visualização, manipulação e renderização de cenas tridimensionais baseadas em arquivos POV-Ray.

---

## Descrição do Projeto

O **3D Pov-Ray Suite** é um aplicativo desenvolvido como módulo da plataforma NetBeans. Ele visa proporcionar uma experiência prática e interativa para usuários que trabalham com arquivos POV-Ray, permitindo:

- Edição de arquivos `.pov` e `.inc` com recursos avançados, como realce de sintaxe e autocompletar.
- Renderização integrada, exibindo os resultados diretamente na IDE.
- Organização e categorização de projetos 3D.
- Exportação de imagens renderizadas.

---

## Tecnologias Utilizadas

- **Java**: Linguagem principal para desenvolvimento.
- **NetBeans Platform**: Framework modular para construção do aplicativo.
- **POV-Ray Language**: Linguagem de script para modelagem 3D.
- **Rendering External Process Integration**: Integração com o renderizador POV-Ray.
- **File Type Recognition**: Reconhecimento e associação de arquivos `.pov` e `.inc`.

---

## Funcionalidades Principais

1. **Edição de arquivos**: Suporte a realce de sintaxe e autocompletar para arquivos POV-Ray.
2. **Renderização integrada**: Possibilidade de executar e visualizar renderizações diretamente na IDE.
3. **Organização de projetos**: Ferramentas de busca e categorização de cenas.
4. **Barra de progresso**: Indicação visual do status da renderização.
5. **Compatibilidade**: Suporte a arquivos grandes e versões recentes do NetBeans.

---
## Pré-requisitos

- **Java JDK 17**
- **NetBeans IDE 17**
- **POV-Ray** instalado e configurado no `PATH` do sistema

---

Aqui está o texto formatado para você incluir no seu arquivo `README.md` usando Markdown:

---

## Apêndice: Configurando o POV-Ray

### MacOS
Usuários de Mac podem achar o **DarwinPorts** a maneira mais fácil de instalar o POV-Ray. Basta seguir os passos abaixo:
1. Instale o **DarwinPorts**.
2. Execute o comando:  
   ```bash
   sudo port install povray
   ```

---

### Linux e Outros Sistemas Unix
Usuários de Linux e outros Unix podem baixar o POV-Ray diretamente do site oficial ([povray.org](http://povray.org)) e seguir as instruções de instalação.  
- O software deve funcionar imediatamente, sem ajustes ou configurações adicionais.  
- **Nota importante**: Certifique-se de que o inicializador do POV-Ray tenha as permissões corretas para ser executado. Você pode verificar isso rodando o POV-Ray na linha de comando.

---

### Windows
Configurar o POV-Ray no Windows exige um pouco mais de atenção. Siga as etapas abaixo para garantir uma instalação correta:

1. **Baixe o POV-Ray**  
   Obtenha a versão estável mais recente para Windows em: [http://povray.org/download](http://povray.org/download)

2. **Instale o POV-Ray**  
   - Execute o instalador e escolha um diretório de instalação como `C:\POVRay`.  
     **Atenção**: Evite diretórios com espaços no caminho, como `C:\Arquivos de Programas`.
   - Exemplo de diretório sugerido:  
     ```plaintext
     C:\POVRay
     ```

3. **Configure o Aplicativo**  
   Após instalar, abra o aplicativo POV-Ray e configure as opções abaixo:  
   - **Desabilite Restrições de E/S**:  
     Vá para:  
     `Opções` → `Restrições de E/S de script` → **Sem restrições**.
     
     ![image](https://github.com/user-attachments/assets/c43b6b44-64e8-46b0-a0e8-d218c4ee169f)

   - **Configure para sair ao finalizar a renderização**:  
     Vá para:  
     `Renderizar` → `Ao concluir` → **Sair do POV-Ray para Windows**.
     
     ![image](https://github.com/user-attachments/assets/5e9cabc2-890f-4808-a9e4-11a2e4a2d52e)


4. **Evite Caminhos com Espaços**  
   - Certifique-se de que os projetos criados estejam em diretórios sem espaços no caminho (ex.: `C:\Projetos\POVRay`).  
   - O nome dos projetos também não deve conter espaços.

---

Se precisar de mais informações, consulte a [documentação oficial do POV-Ray](http://povray.org).

## Autores

- **Caio Oliveira França dos Anjos**
- **Matheus Pedro da Silva**

Desenvolvido no Instituto de Computação - Universidade Federal de Alagoas (UFAL).

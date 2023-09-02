package br.com.viasoft.fiscal.fintech.conta;

import br.com.viasoft.fiscal.configuracaoCobranca.configuracaoCobrancaFilial.ConfiguracaoCobrancaFilial;
import br.com.viasoft.fiscal.configuracaoCobranca.configuracaoCobrancaFilial.ConfiguracaoCobrancaFilialData;
import br.com.viasoft.fiscal.configuracaoPortador.ConfiguracaoPortadorContaBancaria;
import br.com.viasoft.fiscal.fintech.boletos.banck_slip.InfoAssignorAccountDTO;
import br.com.viasoft.fiscal.fintech.http.FintechAbstractService;
import br.com.viasoft.fiscal.fintech.model.dto.CheckAccountDTO;
import br.com.viasoft.fiscal.fintech.model.dto.CheckContaDTO;
import br.com.viasoft.fiscal.fintech.model.dto.ContaTecnospeedDTO;
import br.com.viasoft.fiscal.fintech.model.dto.ConvenioTecnospeedDTO;

import java.util.Map;

public abstract class AbstractContaFintechService extends FintechAbstractService {



    protected abstract String getUrlCadastro(String id, String cedente);
    protected abstract String getUrlCadastroAssignor();

    protected abstract String getUrlInativar(String id);

    protected abstract ConfiguracaoCobrancaFilialData getConfiguracaoCobrancaFilialData();

    protected void cadastrar(ConfiguracaoPortadorContaBancaria configuracaoPortadorContaBancaria) throws Exception {
        var configuracaoCobranca = configuracaoPortadorContaBancaria.getConfiguracaoCobranca();
        var conta = new AssignorAccountDTO(configuracaoPortadorContaBancaria);
        var configuracaoFilial = configuracaoCobranca.getFiliais().stream().findFirst().orElse(new ConfiguracaoCobrancaFilial());
        var numeroTeste = configuracaoFilial.getNumeroConvenio();
        //var configuracaoCobranca = configuracaoPortadorContaBancaria.getConfiguracaoCobranca();
        for (ConfiguracaoCobrancaFilial cobrancaFilial : configuracaoCobranca.getFiliais()) {
            var numero = cobrancaFilial.getNumeroConvenio();
            if (!cobrancaFilial.getInativo()) {
                var urlCadastro = getUrlCadastro(cobrancaFilial.getCodigoConta(), cobrancaFilial.getFilial().getCnpj());
                var token = this.getTokenBearer();
                var checkAccountDTO =  CheckAccountDTO.builder()
                        .agencia(configuracaoPortadorContaBancaria.getNumeroAgencia())
                        .agenciaDv(configuracaoPortadorContaBancaria.getDigitoAgencia())
                        .conta(configuracaoPortadorContaBancaria.getNumeroConta())
                        .contaDv(configuracaoPortadorContaBancaria.getDigitoConta())
                        .codigoBanco(configuracaoPortadorContaBancaria.getBanco().getBanco())
                        .build();
                CheckContaDTO checkConta = checkConta(checkAccountDTO,
                        cobrancaFilial.getFilial().getCnpj(), getUrlCadastroAssignor());
                if (checkConta != null) {
                    conta.setAccountId(checkConta.getAccountId());
                    for (ConvenioTecnospeedDTO convenioTecnospeedDTO : checkConta.getContaTecnospeedDTO().getConvenioTecnospeedDTOList()){
                        if (convenioTecnospeedDTO.getNumeroConvenio().equals(numero)) {
                            //cobrancaFilial.getCodigoConvenio();
                            System.out.println("DIOGO PARABENS SENIOR");
                        }
                    }
                    var contaRetorno = callPut(
                            urlCadastro,
                            conta,
                            InfoAssignorAccountDTO.class,
                            InfoAssignorAccountDTO::getObject,
                            Map.of("Authorization", "Bearer " + token));
                    cobrancaFilial.setCodigoConta(checkConta.getAccountReferenceId());
                    getConfiguracaoCobrancaFilialData().saveAndFlush(cobrancaFilial);
                } else {
                   var contaRetorno = callPost(
                            urlCadastro,
                            conta,
                            InfoAssignorAccountDTO.class,
                            InfoAssignorAccountDTO::getObject,
                            Map.of("Authorization", "Bearer " + token));
                   cobrancaFilial.setCodigoConta(checkConta.getAccountReferenceId());
                   getConfiguracaoCobrancaFilialData().saveAndFlush(cobrancaFilial);
               }
            }
        }
    }
}

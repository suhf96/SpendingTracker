package hu.gyuriczaadam.sprintformteszt.domain.use_case

import hu.gyuriczaadam.sprintformteszt.domain.repositories.TransactionRepository
import hu.gyuriczaadam.sprintformteszt.util.Resource
import hu.gyuriczaadam.sprintformteszt.util.UIText
import kotlinx.coroutines.flow.flow
import okio.IOException
import hu.gyuriczaadam.sprintformteszt.R
import hu.gyuriczaadam.sprintformteszt.data.remote.transaction_dto.toTransactionEntity
import retrofit2.HttpException
import javax.inject.Inject

class GetTransactionsFromApiUseCase @Inject constructor(private val repository: TransactionRepository) {

    operator fun invoke() = flow {
        try {
            emit(Resource.Loading())
            val transactions = repository.getTransactionsFromApi().map {it.toTransactionEntity()}
            val transactionsList= transactions.map {repository.insertTransaction(it)}
            emit(Resource.Success(transactionsList))
        }catch (e:HttpException){
            emit(Resource.Error(UIText.DynamicString(e.localizedMessage)))
        }catch (e:IOException){
            emit(Resource.Error(UIText.StringResource(R.string.network_error_text)))
        }
    }

}
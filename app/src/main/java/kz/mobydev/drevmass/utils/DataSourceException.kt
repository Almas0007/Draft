package kz.mobydev.drevmass.utils


open class DataSourceException(message: String? = null) : Exception(message)

class RemoteDataNotFoundException : DataSourceException("Данные не были найдены из бэк сервисов")
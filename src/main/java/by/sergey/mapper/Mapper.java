package by.sergey.mapper;

public interface Mapper<F, T>{
    T mapFrom(F object);
}

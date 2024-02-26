import('../../../../styles/peopleItem.css')

export const ButtonDaenery = ({roles,callback}) => {
    return(
        <button onClick={callback} className={'btn bg-primary'}>
            <span className='buttonName'>Деканат</span>
        </button>
    )
}

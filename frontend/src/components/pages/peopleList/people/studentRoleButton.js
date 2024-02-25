import('../../../../styles/peopleItem.css')

export const ButtonStudent = ({callback}) => {
    return(
        <button onClick={callback} className={'btn text-bg-warning'}>
            <span className='buttonName'>Студент</span>
        </button>
    )
}